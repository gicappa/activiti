/*
 * Copyright ${project.inceptionYear}-2020 ${project.organization.name}.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.activiti.editor.language.json.converter.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.activiti.editor.constants.EditorJsonConstants;
import org.activiti.editor.constants.StencilConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverterUtil;

public class JsonConverterUtil implements EditorJsonConstants, StencilConstants {

  public static String getPropertyValueAsString(String name, JsonNode objectNode) {
    String propertyValue = null;
    var propertyNode = getProperty(name, objectNode);
    if (propertyNode != null && !"null".equalsIgnoreCase(propertyNode.asText())) {
      propertyValue = propertyNode.asText();
    }
    return propertyValue;
  }

  public static boolean getPropertyValueAsBoolean(String name, JsonNode objectNode) {
    return getPropertyValueAsBoolean(name, objectNode, false);
  }

  public static boolean getPropertyValueAsBoolean(String name, JsonNode objectNode,
    boolean defaultValue) {
    boolean result = defaultValue;
    var stringValue = getPropertyValueAsString(name, objectNode);

    if (PROPERTY_VALUE_YES.equalsIgnoreCase(stringValue) || "true".equalsIgnoreCase(stringValue)) {
      result = true;
    } else if (PROPERTY_VALUE_NO.equalsIgnoreCase(stringValue) || "false".equalsIgnoreCase(
      stringValue)) {
      result = false;
    }

    return result;
  }

  public static List<String> getPropertyValueAsList(String name, JsonNode objectNode) {
    var resultList = new ArrayList<String>();
    var propertyNode = getProperty(name, objectNode);
    if (propertyNode != null && !"null".equalsIgnoreCase(propertyNode.asText())) {
      var propertyValue = propertyNode.asText();
      var valueList = propertyValue.split(",");
      for (var value : valueList) {
        resultList.add(value.trim());
      }
    }
    return resultList;
  }

  public static JsonNode getProperty(String name, JsonNode objectNode) {
    JsonNode propertyNode = null;
    if (objectNode.get(EDITOR_SHAPE_PROPERTIES) != null) {
      var propertiesNode = objectNode.get(EDITOR_SHAPE_PROPERTIES);
      propertyNode = propertiesNode.get(name);
    }
    return propertyNode;
  }

  /**
   * Usable for BPMN 2.0 editor json: traverses all child shapes (also nested), goes into the
   * properties and sees if there is a matching property in the 'properties' of the childshape and
   * returns those in a list.
   * <p>
   * Returns a map with said json nodes, with the key the name of the childshape.
   */

  protected static List<JsonLookupResult> getBpmnProcessModelChildShapesPropertyValues(
    JsonNode editorJsonNode, String propertyName, List<String> allowedStencilTypes) {
    List<JsonLookupResult> result = new ArrayList<JsonLookupResult>();
    internalGetBpmnProcessChildShapePropertyValues(editorJsonNode, propertyName,
      allowedStencilTypes, result);
    return result;
  }

  protected static void internalGetBpmnProcessChildShapePropertyValues(JsonNode editorJsonNode,
    String propertyName,
    List<String> allowedStencilTypes, List<JsonLookupResult> result) {

    var childShapesNode = editorJsonNode.get("childShapes");
    if (childShapesNode != null && childShapesNode.isArray()) {
      var childShapesArrayNode = (ArrayNode) childShapesNode;
      for (JsonNode childShapeNode : childShapesArrayNode) {
        String childShapeNodeStencilId = BpmnJsonConverterUtil.getStencilId(childShapeNode);
        boolean readPropertiesNode = allowedStencilTypes.contains(childShapeNodeStencilId);

        if (readPropertiesNode) {
          // Properties
          var properties = childShapeNode.get("properties");
          if (properties != null && properties.has(propertyName)) {
            var nameNode = properties.get("name");
            var propertyNode = properties.get(propertyName);
            result.add(new JsonLookupResult(BpmnJsonConverterUtil.getElementId(childShapeNode),
              nameNode != null ? nameNode.asText() : null, propertyNode));
          }
        }

        // Potential nested child shapes
        if (childShapeNode.has("childShapes")) {
          internalGetBpmnProcessChildShapePropertyValues(childShapeNode, propertyName,
            allowedStencilTypes, result);
        }

      }
    }
  }

  public static List<JsonLookupResult> getBpmnProcessModelFormReferences(JsonNode editorJsonNode) {
    var allowedStencilTypes = new ArrayList<String>();
    allowedStencilTypes.add(STENCIL_TASK_USER);
    allowedStencilTypes.add(STENCIL_EVENT_START_NONE);
    return getBpmnProcessModelChildShapesPropertyValues(editorJsonNode, "formreference",
      allowedStencilTypes);
  }

  public static List<JsonLookupResult> getBpmnProcessModelDecisionTableReferences(
    JsonNode editorJsonNode) {
    var allowedStencilTypes = new ArrayList<String>();
    allowedStencilTypes.add(STENCIL_TASK_DECISION);
    return getBpmnProcessModelChildShapesPropertyValues(editorJsonNode,
      "decisiontaskdecisiontablereference", allowedStencilTypes);
  }

  // APP MODEL

  public static List<JsonNode> getAppModelReferencedProcessModels(JsonNode appModelJson) {
    var result = new ArrayList<JsonNode>();
    if (appModelJson.has("models")) {
      ArrayNode modelsArrayNode = (ArrayNode) appModelJson.get("models");
      for (JsonNode jsonNode : modelsArrayNode) {
        result.add(jsonNode);
      }
    }
    return result;
  }

  public static Set<String> getAppModelReferencedModelIds(JsonNode appModelJson) {
    if (appModelJson.has("models")) {
      return JsonConverterUtil.gatherStringPropertyFromJsonNodes(appModelJson.get("models"), "id");
    }
    return Collections.emptySet();
  }

  // GENERIC

  /**
   * Loops through a list of {@link JsonNode} instances, and stores the given property with given
   * type in the returned list.
   * <p>
   * In Java 8, this probably could be done a lot cooler.
   */
  public static Set<Long> gatherLongPropertyFromJsonNodes(Iterable<JsonNode> jsonNodes,
    String propertyName) {
    var result = new HashSet<Long>(); // Using a Set to filter out doubles
    for (var node : jsonNodes) {
      if (node.has(propertyName)) {
        var propertyValue = node.get(propertyName).asLong();
        if (propertyValue > 0) { // Just to be safe
          result.add(propertyValue);
        }
      }
    }
    return result;
  }

  public static Set<String> gatherStringPropertyFromJsonNodes(Iterable<JsonNode> jsonNodes,
    String propertyName) {
    var result = new HashSet<String>(); // Using a Set to filter out doubles
    for (var node : jsonNodes) {
      if (node.has(propertyName)) {
        var propertyValue = node.get(propertyName).asText();
        if (propertyValue != null) { // Just to be safe
          result.add(propertyValue);
        }
      }
    }
    return result;
  }

  public static List<JsonNode> filterOutJsonNodes(List<JsonLookupResult> lookupResults) {
    var jsonNodes = new ArrayList<JsonNode>(lookupResults.size());
    for (var lookupResult : lookupResults) {
      jsonNodes.add(lookupResult.getJsonNode());
    }
    return jsonNodes;
  }

  // Helper classes

  public static class JsonLookupResult {

    private String id;
    private String name;
    private JsonNode jsonNode;

    public JsonLookupResult(String id, String name, JsonNode jsonNode) {
      this(name, jsonNode);
      this.id = id;
    }

    public JsonLookupResult(String name, JsonNode jsonNode) {
      this.name = name;
      this.jsonNode = jsonNode;
    }

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public JsonNode getJsonNode() {
      return jsonNode;
    }

    public void setJsonNode(JsonNode jsonNode) {
      this.jsonNode = jsonNode;
    }

  }

}
