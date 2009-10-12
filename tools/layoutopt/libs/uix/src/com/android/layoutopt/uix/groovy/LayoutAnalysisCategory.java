/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.layoutopt.uix.groovy;

import com.android.layoutopt.uix.LayoutAnalysis;
import com.android.layoutopt.uix.LayoutNode;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import groovy.lang.GString;
import groovy.xml.dom.DOMCategory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

/**
 * Support class for Groovy rules. This class adds new Groovy capabilities
 * to {@link com.android.layoutopt.uix.LayoutAnalysis} and {@link org.w3c.dom.Node}.
 */
public class LayoutAnalysisCategory {
    public static List<Node> all(Element element) {
        NodeList list = DOMCategory.depthFirst(element);
        int count = list.getLength();
        List<Node> nodes = new ArrayList<Node>(count - 1);
        for (int i = 1; i < count; i++) {
            nodes.add(list.item(i));
        }
        return nodes;
    }
    
    /**
     * xmlNode.isRoot()
     */
    public static boolean isRoot(Node node) {
        return node.getOwnerDocument().getDocumentElement() == node;
    }

    /**
     * xmlNode.is("tagName")
     */
    public static boolean is(Node node, String name) {
        return node.getNodeName().equals(name);
    }

    /**
     * xmlNode.depth()
     */
    public static int depth(Node node) {
        int maxDepth = 0;
        NodeList list = node.getChildNodes();
        int count = list.getLength();

        for (int i = 0; i < count; i++) {
            maxDepth = Math.max(maxDepth, depth(list.item(i)));
        }

        return maxDepth + 1;
    }

    /**
     * analysis << "The issue"
     */
    public static LayoutAnalysis leftShift(LayoutAnalysis analysis, GString description) {
        analysis.addIssue(description.toString());
        return analysis;
    }

    /**
     * analysis << [node: node, description: "The issue"]
     */
    public static LayoutAnalysis leftShift(LayoutAnalysis analysis, Map issue) {
        analysis.addIssue((LayoutNode) issue.get("node"), issue.get("description").toString());
        return analysis;
    }
}