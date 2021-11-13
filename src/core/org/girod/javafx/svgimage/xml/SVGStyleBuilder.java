/*
Copyright (c) 2021, Hervé Girod
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this
   list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice,
   this list of conditions and the following disclaimer in the documentation
   and/or other materials provided with the distribution.

3. Neither the name of the copyright holder nor the names of its
   contributors may be used to endorse or promote products derived from
   this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

Alternatively if you have any questions about this project, you can visit
the project website at the project page on https://github.com/hervegirod/fxsvgimage
 */
package org.girod.javafx.svgimage.xml;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import javafx.scene.transform.Transform;

/**
 * This class parse a style declaration.
 *
 * @version 0.5.4
 */
public class SVGStyleBuilder implements SVGTags {
   private static final Pattern STYLES = Pattern.compile("\\.[a-zA-Z_][a-zA-Z0-9_\\-]*\\s*\\{[a-zA-Z0-9_\\-+\\.\\s,:\\#;]+\\}\\s*");
   private static final Pattern RULE_CONTENT = Pattern.compile("[a-zA-Z_][a-zA-Z0-9_\\-]*\\s*:\\s*[a-zA-Z0-9_\\-+,\\.\\#\\.]*");

   private SVGStyleBuilder() {
   }

   public static Styles parseStyle(String content, Viewport viewport) {
      Matcher m = STYLES.matcher(content);
      Styles styles = null;
      while (m.find()) {
         if (styles == null) {
            styles = new Styles();
         }
         String theRule = m.group();
         int parIndex = theRule.indexOf('{');
         String styleClass = theRule.substring(1, parIndex).trim();
         Styles.Rule rule = new Styles.Rule(styleClass);
         boolean isEmpty = true;
         String ruleContent = theRule.substring(parIndex + 1, theRule.length() - 2).trim();
         Matcher m2 = RULE_CONTENT.matcher(ruleContent);
         while (m2.find()) {
            if (isEmpty) {
               styles.addRule(rule);
               isEmpty = false;
            }
            String theProperty = m2.group();
            int index = theProperty.indexOf(':');
            String key = theProperty.substring(0, index).trim();
            String value = theProperty.substring(index + 1, theProperty.length()).trim();
            switch (key) {
               case FILL: {
                  Color col = ParserUtils.getColor(value);
                  rule.addProperty(key, Styles.FILL, col);
                  break;
               }
               case STROKE: {
                  Color col = ParserUtils.getColor(value);
                  rule.addProperty(key, Styles.STROKE, col);
                  break;
               }
               case STROKE_WIDTH: {
                  double width = ParserUtils.parseDoubleProtected(value, true, viewport);
                  rule.addProperty(key, Styles.STROKE_WIDTH, width);
                  break;
               }
               case STROKE_DASHARRAY: {
                  List<Double> list = ParserUtils.parseDashArray(value, viewport);
                  if (list != null) {
                     rule.addProperty(key, Styles.STROKE_DASHARRAY, list);
                  }
                  break;
               }
               case FONT_FAMILY: {
                  String fontFamily = value.replace("'", "");
                  rule.addProperty(key, Styles.FONT_FAMILY, fontFamily);
                  break;
               }
               case FONT_WEIGHT: {
                  FontWeight fontWeight = SVGShapeBuilder.getFontWeight(value);
                  rule.addProperty(key, Styles.FONT_WEIGHT, fontWeight);
                  break;
               }
               case FONT_STYLE: {
                  ExtendedFontPosture fontPosture = SVGShapeBuilder.getExtendedFontPosture(value);
                  rule.addProperty(key, Styles.FONT_STYLE, fontPosture);
                  break;
               }
               case FONT_SIZE: {
                  double size = ParserUtils.parseFontSize(value);
                  rule.addProperty(key, Styles.FONT_SIZE, size);
                  break;
               }
               case TEXT_DECORATION: {
                  rule.addProperty(key, Styles.TEXT_DECORATION, value);
                  break;
               }
               case OPACITY: {
                  double opacity = ParserUtils.parseOpacity(value);
                  if (opacity >= 0) {
                     rule.addProperty(key, Styles.OPACITY, opacity);
                  }
                  break;
               }
               case FILL_OPACITY: {
                  double opacity = ParserUtils.parseOpacity(value);
                  if (opacity >= 0) {
                     rule.addProperty(key, Styles.FILL_OPACITY, opacity);
                  }
                  break;
               }
               case TRANSFORM: {
                  List<Transform> transformList = ParserUtils.extractTransforms(value, viewport);
                  if (!transformList.isEmpty()) {
                     rule.addProperty(key, Styles.TRANSFORM, transformList);
                  }
                  break;
               }
            }
         }
      }
      return styles;
   }
}
