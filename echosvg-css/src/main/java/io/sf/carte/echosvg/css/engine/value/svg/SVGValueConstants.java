/*

   See the NOTICE file distributed with this work for additional
   information regarding copyright ownership.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */
package io.sf.carte.echosvg.css.engine.value.svg;

import org.w3c.css.om.unit.CSSUnit;

import io.sf.carte.echosvg.css.engine.value.FloatValue;
import io.sf.carte.echosvg.css.engine.value.IdentValue;
import io.sf.carte.echosvg.css.engine.value.NumericValue;
import io.sf.carte.echosvg.css.engine.value.RGBColorValue;
import io.sf.carte.echosvg.css.engine.value.Value;
import io.sf.carte.echosvg.css.engine.value.ValueConstants;
import io.sf.carte.echosvg.util.CSSConstants;

/**
 * This interface provides constants for SVG values.
 *
 * <p>
 * Original author: <a href="mailto:stephane@hillion.org">Stephane Hillion</a>.
 * For later modifications, see Git history.
 * </p>
 * @version $Id$
 */
public interface SVGValueConstants extends ValueConstants {

	/**
	 * 0 degree
	 */
	NumericValue ZERO_DEGREE = FloatValue.createConstant(CSSUnit.CSS_DEG, 0);

	/**
	 * 1
	 */
	NumericValue NUMBER_1 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 1);

	/**
	 * 4
	 */
	NumericValue NUMBER_4 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 4);

	/**
	 * 11
	 */
	NumericValue NUMBER_11 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 11);

	/**
	 * 19
	 */
	NumericValue NUMBER_19 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 19);

	/**
	 * 20
	 */
	NumericValue NUMBER_20 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 20);

	/**
	 * 21
	 */
	NumericValue NUMBER_21 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 21);

	/**
	 * 25
	 */
	NumericValue NUMBER_25 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 25);

	/**
	 * 30
	 */
	NumericValue NUMBER_30 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 30);

	/**
	 * 32
	 */
	NumericValue NUMBER_32 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 32);

	/**
	 * 34
	 */
	NumericValue NUMBER_34 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 34);

	/**
	 * 35
	 */
	NumericValue NUMBER_35 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 35);

	/**
	 * 42
	 */
	NumericValue NUMBER_42 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 42);

	/**
	 * 43
	 */
	NumericValue NUMBER_43 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 43);

	/**
	 * 45
	 */
	NumericValue NUMBER_45 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 45);

	/**
	 * 46
	 */
	NumericValue NUMBER_46 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 46);

	/**
	 * 47
	 */
	NumericValue NUMBER_47 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 47);

	/**
	 * 50
	 */
	NumericValue NUMBER_50 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 50);

	/**
	 * 60
	 */
	NumericValue NUMBER_60 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 60);

	/**
	 * 61
	 */
	NumericValue NUMBER_61 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 61);

	/**
	 * 63
	 */
	NumericValue NUMBER_63 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 63);

	/**
	 * 64
	 */
	NumericValue NUMBER_64 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 64);

	/**
	 * 65
	 */
	NumericValue NUMBER_65 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 65);

	/**
	 * 69
	 */
	NumericValue NUMBER_69 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 69);

	/**
	 * 70
	 */
	NumericValue NUMBER_70 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 70);

	/**
	 * 71
	 */
	NumericValue NUMBER_71 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 71);

	/**
	 * 72
	 */
	NumericValue NUMBER_72 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 72);

	/**
	 * 75
	 */
	NumericValue NUMBER_75 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 75);

	/**
	 * 79
	 */
	NumericValue NUMBER_79 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 79);

	/**
	 * 80
	 */
	NumericValue NUMBER_80 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 80);

	/**
	 * 82
	 */
	NumericValue NUMBER_82 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 82);

	/**
	 * 85
	 */
	NumericValue NUMBER_85 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 85);

	/**
	 * 87
	 */
	NumericValue NUMBER_87 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 87);

	/**
	 * 90
	 */
	NumericValue NUMBER_90 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 90);

	/**
	 * 91
	 */
	NumericValue NUMBER_91 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 91);

	/**
	 * 92
	 */
	NumericValue NUMBER_92 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 92);

	/**
	 * 95
	 */
	NumericValue NUMBER_95 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 95);

	/**
	 * 96
	 */
	NumericValue NUMBER_96 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 96);

	/**
	 * 99
	 */
	NumericValue NUMBER_99 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 99);

	/**
	 * 102
	 */
	NumericValue NUMBER_102 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 102);

	/**
	 * 104
	 */
	NumericValue NUMBER_104 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 104);

	/**
	 * 105
	 */
	NumericValue NUMBER_105 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 105);

	/**
	 * 106
	 */
	NumericValue NUMBER_106 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 106);

	/**
	 * 107
	 */
	NumericValue NUMBER_107 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 107);

	/**
	 * 112
	 */
	NumericValue NUMBER_112 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 112);

	/**
	 * 113
	 */
	NumericValue NUMBER_113 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 113);

	/**
	 * 114
	 */
	NumericValue NUMBER_114 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 114);

	/**
	 * 119
	 */
	NumericValue NUMBER_119 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 119);

	/**
	 * 122
	 */
	NumericValue NUMBER_122 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 122);

	/**
	 * 123
	 */
	NumericValue NUMBER_123 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 123);

	/**
	 * 124
	 */
	NumericValue NUMBER_124 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 124);

	/**
	 * 127
	 */
	NumericValue NUMBER_127 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 127);

	/**
	 * 130
	 */
	NumericValue NUMBER_130 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 130);

	/**
	 * 133
	 */
	NumericValue NUMBER_133 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 133);

	/**
	 * 134
	 */
	NumericValue NUMBER_134 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 134);

	/**
	 * 135
	 */
	NumericValue NUMBER_135 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 135);

	/**
	 * 136
	 */
	NumericValue NUMBER_136 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 136);

	/**
	 * 138
	 */
	NumericValue NUMBER_138 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 138);

	/**
	 * 139
	 */
	NumericValue NUMBER_139 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 139);

	/**
	 * 140
	 */
	NumericValue NUMBER_140 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 140);

	/**
	 * 142
	 */
	NumericValue NUMBER_142 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 142);

	/**
	 * 143
	 */
	NumericValue NUMBER_143 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 143);

	/**
	 * 144
	 */
	NumericValue NUMBER_144 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 144);

	/**
	 * 147
	 */
	NumericValue NUMBER_147 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 147);

	/**
	 * 148
	 */
	NumericValue NUMBER_148 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 148);

	/**
	 * 149
	 */
	NumericValue NUMBER_149 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 149);

	/**
	 * 150
	 */
	NumericValue NUMBER_150 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 150);

	/**
	 * 152
	 */
	NumericValue NUMBER_152 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 152);

	/**
	 * 153
	 */
	NumericValue NUMBER_153 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 153);

	/**
	 * 154
	 */
	NumericValue NUMBER_154 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 154);

	/**
	 * 158
	 */
	NumericValue NUMBER_158 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 158);

	/**
	 * 160
	 */
	NumericValue NUMBER_160 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 160);

	/**
	 * 164
	 */
	NumericValue NUMBER_164 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 164);

	/**
	 * 165
	 */
	NumericValue NUMBER_165 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 165);

	/**
	 * 169
	 */
	NumericValue NUMBER_169 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 169);

	/**
	 * 170
	 */
	NumericValue NUMBER_170 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 170);

	/**
	 * 173
	 */
	NumericValue NUMBER_173 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 173);

	/**
	 * 175
	 */
	NumericValue NUMBER_175 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 175);

	/**
	 * 176
	 */
	NumericValue NUMBER_176 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 176);

	/**
	 * 178
	 */
	NumericValue NUMBER_178 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 178);

	/**
	 * 179
	 */
	NumericValue NUMBER_179 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 179);

	/**
	 * 180
	 */
	NumericValue NUMBER_180 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 180);

	/**
	 * 181
	 */
	NumericValue NUMBER_181 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 181);

	/**
	 * 182
	 */
	NumericValue NUMBER_182 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 182);

	/**
	 * 183
	 */
	NumericValue NUMBER_183 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 183);

	/**
	 * 184
	 */
	NumericValue NUMBER_184 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 184);

	/**
	 * 185
	 */
	NumericValue NUMBER_185 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 185);

	/**
	 * 186
	 */
	NumericValue NUMBER_186 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 186);

	/**
	 * 188
	 */
	NumericValue NUMBER_188 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 188);

	/**
	 * 189
	 */
	NumericValue NUMBER_189 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 189);

	/**
	 * 191
	 */
	NumericValue NUMBER_191 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 191);

	/**
	 * 193
	 */
	NumericValue NUMBER_193 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 193);

	/**
	 * 196
	 */
	NumericValue NUMBER_196 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 196);

	/**
	 * 199
	 */
	NumericValue NUMBER_199 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 199);

	/**
	 * 203
	 */
	NumericValue NUMBER_203 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 203);

	/**
	 * 204
	 */
	NumericValue NUMBER_204 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 204);

	/**
	 * 205
	 */
	NumericValue NUMBER_205 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 205);

	/**
	 * 206
	 */
	NumericValue NUMBER_206 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 206);

	/**
	 * 208
	 */
	NumericValue NUMBER_208 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 208);

	/**
	 * 209
	 */
	NumericValue NUMBER_209 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 209);

	/**
	 * 210
	 */
	NumericValue NUMBER_210 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 210);

	/**
	 * 211
	 */
	NumericValue NUMBER_211 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 211);

	/**
	 * 212
	 */
	NumericValue NUMBER_212 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 212);

	/**
	 * 213
	 */
	NumericValue NUMBER_213 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 213);

	/**
	 * 214
	 */
	NumericValue NUMBER_214 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 214);

	/**
	 * 215
	 */
	NumericValue NUMBER_215 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 215);

	/**
	 * 216
	 */
	NumericValue NUMBER_216 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 216);

	/**
	 * 218
	 */
	NumericValue NUMBER_218 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 218);

	/**
	 * 219
	 */
	NumericValue NUMBER_219 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 219);

	/**
	 * 220
	 */
	NumericValue NUMBER_220 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 220);

	/**
	 * 221
	 */
	NumericValue NUMBER_221 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 221);

	/**
	 * 222
	 */
	NumericValue NUMBER_222 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 222);

	/**
	 * 224
	 */
	NumericValue NUMBER_224 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 224);

	/**
	 * 225
	 */
	NumericValue NUMBER_225 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 225);

	/**
	 * 226
	 */
	NumericValue NUMBER_226 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 226);

	/**
	 * 228
	 */
	NumericValue NUMBER_228 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 228);

	/**
	 * 230
	 */
	NumericValue NUMBER_230 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 230);

	/**
	 * 232
	 */
	NumericValue NUMBER_232 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 232);

	/**
	 * 233
	 */
	NumericValue NUMBER_233 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 233);

	/**
	 * 235
	 */
	NumericValue NUMBER_235 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 235);

	/**
	 * 237
	 */
	NumericValue NUMBER_237 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 237);

	/**
	 * 238
	 */
	NumericValue NUMBER_238 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 238);

	/**
	 * 239
	 */
	NumericValue NUMBER_239 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 239);

	/**
	 * 240
	 */
	NumericValue NUMBER_240 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 240);

	/**
	 * 244
	 */
	NumericValue NUMBER_244 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 244);

	/**
	 * 245
	 */
	NumericValue NUMBER_245 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 245);

	/**
	 * 248
	 */
	NumericValue NUMBER_248 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 248);

	/**
	 * 250
	 */
	NumericValue NUMBER_250 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 250);

	/**
	 * 251
	 */
	NumericValue NUMBER_251 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 251);

	/**
	 * 252
	 */
	NumericValue NUMBER_252 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 252);

	/**
	 * 253
	 */
	NumericValue NUMBER_253 = FloatValue.createConstant(CSSUnit.CSS_NUMBER, 253);

	/**
	 * The 'accumulate' keyword.
	 */
	Value ACCUMULATE_VALUE = IdentValue.createConstant(CSSConstants.CSS_ACCUMULATE_VALUE);

	/**
	 * The 'after-edge' keyword.
	 */
	Value AFTER_EDGE_VALUE = IdentValue.createConstant(CSSConstants.CSS_AFTER_EDGE_VALUE);

	/**
	 * The 'alphabetic' keyword.
	 */
	Value ALPHABETIC_VALUE = IdentValue.createConstant(CSSConstants.CSS_ALPHABETIC_VALUE);

	/**
	 * The 'baseline' keyword.
	 */
	Value BASELINE_VALUE = IdentValue.createConstant(CSSConstants.CSS_BASELINE_VALUE);

	/**
	 * The 'before-edge' keyword.
	 */
	Value BEFORE_EDGE_VALUE = IdentValue.createConstant(CSSConstants.CSS_BEFORE_EDGE_VALUE);

	/**
	 * The 'bevel' keyword.
	 */
	Value BEVEL_VALUE = IdentValue.createConstant(CSSConstants.CSS_BEVEL_VALUE);

	/**
	 * The 'butt' keyword.
	 */
	Value BUTT_VALUE = IdentValue.createConstant(CSSConstants.CSS_BUTT_VALUE);

	/**
	 * The 'central' keyword.
	 */
	Value CENTRAL_VALUE = IdentValue.createConstant(CSSConstants.CSS_CENTRAL_VALUE);

	/**
	 * The 'currentcolor' keyword.
	 */
	Value CURRENTCOLOR_VALUE = IdentValue.createConstant(CSSConstants.CSS_CURRENTCOLOR_VALUE);

	/**
	 * The 'end' keyword.
	 */
	Value END_VALUE = IdentValue.createConstant(CSSConstants.CSS_END_VALUE);

	/**
	 * The 'evenodd' keyword.
	 */
	Value EVENODD_VALUE = IdentValue.createConstant(CSSConstants.CSS_EVENODD_VALUE);

	/**
	 * The 'fill' keyword.
	 */
	Value FILL_VALUE = IdentValue.createConstant(CSSConstants.CSS_FILL_VALUE);

	/**
	 * The 'fillstroke' keyword.
	 */
	Value FILLSTROKE_VALUE = IdentValue.createConstant(CSSConstants.CSS_FILLSTROKE_VALUE);

	/**
	 * The 'geometricprecision' keyword.
	 */
	Value GEOMETRICPRECISION_VALUE = IdentValue.createConstant(CSSConstants.CSS_GEOMETRICPRECISION_VALUE);

	/**
	 * The 'hanging' keyword.
	 */
	Value HANGING_VALUE = IdentValue.createConstant(CSSConstants.CSS_HANGING_VALUE);

	/**
	 * The 'ideographic' keyword.
	 */
	Value IDEOGRAPHIC_VALUE = IdentValue.createConstant(CSSConstants.CSS_IDEOGRAPHIC_VALUE);

	/**
	 * The 'linearRGB' keyword.
	 */
	Value LINEARRGB_VALUE = IdentValue.createConstant(CSSConstants.CSS_LINEARRGB_VALUE);

	/**
	 * The 'lr' keyword.
	 */
	Value LR_VALUE = IdentValue.createConstant(CSSConstants.CSS_LR_VALUE);

	/**
	 * The 'lr-tb' keyword.
	 */
	Value LR_TB_VALUE = IdentValue.createConstant(CSSConstants.CSS_LR_TB_VALUE);

	/**
	 * The 'mathematical' keyword.
	 */
	Value MATHEMATICAL_VALUE = IdentValue.createConstant(CSSConstants.CSS_MATHEMATICAL_VALUE);

	/**
	 * The 'middle' keyword.
	 */
	Value MIDDLE_VALUE = IdentValue.createConstant(CSSConstants.CSS_MIDDLE_VALUE);

	/**
	 * The 'new' keyword.
	 */
	Value NEW_VALUE = IdentValue.createConstant(CSSConstants.CSS_NEW_VALUE);

	/**
	 * The 'miter' keyword.
	 */
	Value MITER_VALUE = IdentValue.createConstant(CSSConstants.CSS_MITER_VALUE);

	/**
	 * The 'no-change' keyword.
	 */
	Value NO_CHANGE_VALUE = IdentValue.createConstant(CSSConstants.CSS_NO_CHANGE_VALUE);

	/**
	 * The 'nonzero' keyword.
	 */
	Value NONZERO_VALUE = IdentValue.createConstant(CSSConstants.CSS_NONZERO_VALUE);

	/**
	 * The 'optimizeLegibility' keyword.
	 */
	Value OPTIMIZELEGIBILITY_VALUE = IdentValue.createConstant(CSSConstants.CSS_OPTIMIZELEGIBILITY_VALUE);

	/**
	 * The 'optimizeQuality' keyword.
	 */
	Value OPTIMIZEQUALITY_VALUE = IdentValue.createConstant(CSSConstants.CSS_OPTIMIZEQUALITY_VALUE);

	/**
	 * The 'optimizeSpeed' keyword.
	 */
	Value OPTIMIZESPEED_VALUE = IdentValue.createConstant(CSSConstants.CSS_OPTIMIZESPEED_VALUE);

	/**
	 * The 'reset-size' keyword.
	 */
	Value RESET_SIZE_VALUE = IdentValue.createConstant(CSSConstants.CSS_RESET_SIZE_VALUE);

	/**
	 * The 'rl' keyword.
	 */
	Value RL_VALUE = IdentValue.createConstant(CSSConstants.CSS_RL_VALUE);

	/**
	 * The 'rl-tb' keyword.
	 */
	Value RL_TB_VALUE = IdentValue.createConstant(CSSConstants.CSS_RL_TB_VALUE);

	/**
	 * The 'round' keyword.
	 */
	Value ROUND_VALUE = IdentValue.createConstant(CSSConstants.CSS_ROUND_VALUE);

	/**
	 * The 'square' keyword.
	 */
	Value SQUARE_VALUE = IdentValue.createConstant(CSSConstants.CSS_SQUARE_VALUE);

	/**
	 * The 'sRGB' keyword.
	 */
	Value SRGB_VALUE = IdentValue.createConstant(CSSConstants.CSS_SRGB_VALUE);

	/**
	 * The 'start' keyword.
	 */
	Value START_VALUE = IdentValue.createConstant(CSSConstants.CSS_START_VALUE);

	/**
	 * The 'sub' keyword.
	 */
	Value SUB_VALUE = IdentValue.createConstant(CSSConstants.CSS_SUB_VALUE);

	/**
	 * The 'super' keyword.
	 */
	Value SUPER_VALUE = IdentValue.createConstant(CSSConstants.CSS_SUPER_VALUE);

	/**
	 * The 'tb' keyword.
	 */
	Value TB_VALUE = IdentValue.createConstant(CSSConstants.CSS_TB_VALUE);

	/**
	 * The 'tb-rl' keyword.
	 */
	Value TB_RL_VALUE = IdentValue.createConstant(CSSConstants.CSS_TB_RL_VALUE);

	/**
	 * The 'text-after-edge' keyword.
	 */
	Value TEXT_AFTER_EDGE_VALUE = IdentValue.createConstant(CSSConstants.CSS_TEXT_AFTER_EDGE_VALUE);

	/**
	 * The 'text-before-edge' keyword.
	 */
	Value TEXT_BEFORE_EDGE_VALUE = IdentValue.createConstant(CSSConstants.CSS_TEXT_BEFORE_EDGE_VALUE);

	/**
	 * The 'text-bottom' keyword.
	 */
	Value TEXT_BOTTOM_VALUE = IdentValue.createConstant(CSSConstants.CSS_TEXT_BOTTOM_VALUE);

	/**
	 * The 'text-top' keyword.
	 */
	Value TEXT_TOP_VALUE = IdentValue.createConstant(CSSConstants.CSS_TEXT_TOP_VALUE);

	/**
	 * The 'use-script' keyword.
	 */
	Value USE_SCRIPT_VALUE = IdentValue.createConstant(CSSConstants.CSS_USE_SCRIPT_VALUE);

	/**
	 * The 'visiblefill' keyword.
	 */
	Value VISIBLEFILL_VALUE = IdentValue.createConstant(CSSConstants.CSS_VISIBLEFILL_VALUE);

	/**
	 * The 'visiblefillstroke' keyword.
	 */
	Value VISIBLEFILLSTROKE_VALUE = IdentValue.createConstant(CSSConstants.CSS_VISIBLEFILLSTROKE_VALUE);

	/**
	 * The 'visiblepainted' keyword.
	 */
	Value VISIBLEPAINTED_VALUE = IdentValue.createConstant(CSSConstants.CSS_VISIBLEPAINTED_VALUE);

	/**
	 * The 'visiblestroke' keyword.
	 */
	Value VISIBLESTROKE_VALUE = IdentValue.createConstant(CSSConstants.CSS_VISIBLESTROKE_VALUE);

	/**
	 * The 'aliceblue' color name.
	 */
	Value ALICEBLUE_VALUE = IdentValue.createConstant(CSSConstants.CSS_ALICEBLUE_VALUE);

	/**
	 * The 'antiquewhite' color name.
	 */
	Value ANTIQUEWHITE_VALUE = IdentValue.createConstant(CSSConstants.CSS_ANTIQUEWHITE_VALUE);

	/**
	 * The 'aquamarine' color name.
	 */
	Value AQUAMARINE_VALUE = IdentValue.createConstant(CSSConstants.CSS_AQUAMARINE_VALUE);

	/**
	 * The 'azure' color name.
	 */
	Value AZURE_VALUE = IdentValue.createConstant(CSSConstants.CSS_AZURE_VALUE);

	/**
	 * The 'beige' color name.
	 */
	Value BEIGE_VALUE = IdentValue.createConstant(CSSConstants.CSS_BEIGE_VALUE);

	/**
	 * The 'bisque' color name.
	 */
	Value BISQUE_VALUE = IdentValue.createConstant(CSSConstants.CSS_BISQUE_VALUE);

	/**
	 * The 'blanchedalmond' color name.
	 */
	Value BLANCHEDALMOND_VALUE = IdentValue.createConstant(CSSConstants.CSS_BLANCHEDALMOND_VALUE);

	/**
	 * The 'blueviolet' color name.
	 */
	Value BLUEVIOLET_VALUE = IdentValue.createConstant(CSSConstants.CSS_BLUEVIOLET_VALUE);

	/**
	 * The 'brown' color name.
	 */
	Value BROWN_VALUE = IdentValue.createConstant(CSSConstants.CSS_BROWN_VALUE);

	/**
	 * The 'burlywood' color name.
	 */
	Value BURLYWOOD_VALUE = IdentValue.createConstant(CSSConstants.CSS_BURLYWOOD_VALUE);

	/**
	 * The 'cadetblue' color name.
	 */
	Value CADETBLUE_VALUE = IdentValue.createConstant(CSSConstants.CSS_CADETBLUE_VALUE);

	/**
	 * The 'chartreuse' color name.
	 */
	Value CHARTREUSE_VALUE = IdentValue.createConstant(CSSConstants.CSS_CHARTREUSE_VALUE);

	/**
	 * The 'chocolate' color name.
	 */
	Value CHOCOLATE_VALUE = IdentValue.createConstant(CSSConstants.CSS_CHOCOLATE_VALUE);

	/**
	 * The 'coral' color name.
	 */
	Value CORAL_VALUE = IdentValue.createConstant(CSSConstants.CSS_CORAL_VALUE);

	/**
	 * The 'cornflowerblue' color name.
	 */
	Value CORNFLOWERBLUE_VALUE = IdentValue.createConstant(CSSConstants.CSS_CORNFLOWERBLUE_VALUE);

	/**
	 * The 'cornsilk' color name.
	 */
	Value CORNSILK_VALUE = IdentValue.createConstant(CSSConstants.CSS_CORNSILK_VALUE);

	/**
	 * The 'crimson' color name.
	 */
	Value CRIMSON_VALUE = IdentValue.createConstant(CSSConstants.CSS_CRIMSON_VALUE);

	/**
	 * The 'cyan' color name.
	 */
	Value CYAN_VALUE = IdentValue.createConstant(CSSConstants.CSS_CYAN_VALUE);

	/**
	 * The 'darkblue' color name.
	 */
	Value DARKBLUE_VALUE = IdentValue.createConstant(CSSConstants.CSS_DARKBLUE_VALUE);

	/**
	 * The 'darkcyan' color name.
	 */
	Value DARKCYAN_VALUE = IdentValue.createConstant(CSSConstants.CSS_DARKCYAN_VALUE);

	/**
	 * The 'darkgoldenrod' color name.
	 */
	Value DARKGOLDENROD_VALUE = IdentValue.createConstant(CSSConstants.CSS_DARKGOLDENROD_VALUE);

	/**
	 * The 'darkgray' color name.
	 */
	Value DARKGRAY_VALUE = IdentValue.createConstant(CSSConstants.CSS_DARKGRAY_VALUE);

	/**
	 * The 'darkgreen' color name.
	 */
	Value DARKGREEN_VALUE = IdentValue.createConstant(CSSConstants.CSS_DARKGREEN_VALUE);

	/**
	 * The 'darkgrey' color name.
	 */
	Value DARKGREY_VALUE = IdentValue.createConstant(CSSConstants.CSS_DARKGREY_VALUE);

	/**
	 * The 'darkkhaki' color name.
	 */
	Value DARKKHAKI_VALUE = IdentValue.createConstant(CSSConstants.CSS_DARKKHAKI_VALUE);

	/**
	 * The 'darkmagenta' color name.
	 */
	Value DARKMAGENTA_VALUE = IdentValue.createConstant(CSSConstants.CSS_DARKMAGENTA_VALUE);

	/**
	 * The 'darkolivegreen' color name.
	 */
	Value DARKOLIVEGREEN_VALUE = IdentValue.createConstant(CSSConstants.CSS_DARKOLIVEGREEN_VALUE);

	/**
	 * The 'darkorange' color name.
	 */
	Value DARKORANGE_VALUE = IdentValue.createConstant(CSSConstants.CSS_DARKORANGE_VALUE);

	/**
	 * The 'darkorchid' color name.
	 */
	Value DARKORCHID_VALUE = IdentValue.createConstant(CSSConstants.CSS_DARKORCHID_VALUE);

	/**
	 * The 'darkred' color name.
	 */
	Value DARKRED_VALUE = IdentValue.createConstant(CSSConstants.CSS_DARKRED_VALUE);

	/**
	 * The 'darksalmon' color name.
	 */
	Value DARKSALMON_VALUE = IdentValue.createConstant(CSSConstants.CSS_DARKSALMON_VALUE);

	/**
	 * The 'darkseagreen' color name.
	 */
	Value DARKSEAGREEN_VALUE = IdentValue.createConstant(CSSConstants.CSS_DARKSEAGREEN_VALUE);

	/**
	 * The 'darkslateblue' color name.
	 */
	Value DARKSLATEBLUE_VALUE = IdentValue.createConstant(CSSConstants.CSS_DARKSLATEBLUE_VALUE);

	/**
	 * The 'darkslategray' color name.
	 */
	Value DARKSLATEGRAY_VALUE = IdentValue.createConstant(CSSConstants.CSS_DARKSLATEGRAY_VALUE);

	/**
	 * The 'darkslategrey' color name.
	 */
	Value DARKSLATEGREY_VALUE = IdentValue.createConstant(CSSConstants.CSS_DARKSLATEGREY_VALUE);

	/**
	 * The 'darkturquoise' color name.
	 */
	Value DARKTURQUOISE_VALUE = IdentValue.createConstant(CSSConstants.CSS_DARKTURQUOISE_VALUE);

	/**
	 * The 'darkviolet' color name.
	 */
	Value DARKVIOLET_VALUE = IdentValue.createConstant(CSSConstants.CSS_DARKVIOLET_VALUE);

	/**
	 * The 'deeppink' color name.
	 */
	Value DEEPPINK_VALUE = IdentValue.createConstant(CSSConstants.CSS_DEEPPINK_VALUE);

	/**
	 * The 'deepskyblue' color name.
	 */
	Value DEEPSKYBLUE_VALUE = IdentValue.createConstant(CSSConstants.CSS_DEEPSKYBLUE_VALUE);

	/**
	 * The 'dimgray' color name.
	 */
	Value DIMGRAY_VALUE = IdentValue.createConstant(CSSConstants.CSS_DIMGRAY_VALUE);

	/**
	 * The 'dimgrey' color name.
	 */
	Value DIMGREY_VALUE = IdentValue.createConstant(CSSConstants.CSS_DIMGREY_VALUE);

	/**
	 * The 'dodgerblue' color name.
	 */
	Value DODGERBLUE_VALUE = IdentValue.createConstant(CSSConstants.CSS_DODGERBLUE_VALUE);

	/**
	 * The 'firebrick' color name.
	 */
	Value FIREBRICK_VALUE = IdentValue.createConstant(CSSConstants.CSS_FIREBRICK_VALUE);

	/**
	 * The 'floralwhite' color name.
	 */
	Value FLORALWHITE_VALUE = IdentValue.createConstant(CSSConstants.CSS_FLORALWHITE_VALUE);

	/**
	 * The 'forestgreen' color name.
	 */
	Value FORESTGREEN_VALUE = IdentValue.createConstant(CSSConstants.CSS_FORESTGREEN_VALUE);

	/**
	 * The 'gainsboro' color name.
	 */
	Value GAINSBORO_VALUE = IdentValue.createConstant(CSSConstants.CSS_GAINSBORO_VALUE);

	/**
	 * The 'ghostwhite' color name.
	 */
	Value GHOSTWHITE_VALUE = IdentValue.createConstant(CSSConstants.CSS_GHOSTWHITE_VALUE);

	/**
	 * The 'gold' color name.
	 */
	Value GOLD_VALUE = IdentValue.createConstant(CSSConstants.CSS_GOLD_VALUE);

	/**
	 * The 'goldenrod' color name.
	 */
	Value GOLDENROD_VALUE = IdentValue.createConstant(CSSConstants.CSS_GOLDENROD_VALUE);

	/**
	 * The 'greenyellow' color name.
	 */
	Value GREENYELLOW_VALUE = IdentValue.createConstant(CSSConstants.CSS_GREENYELLOW_VALUE);

	/**
	 * The 'grey' color name.
	 */
	Value GREY_VALUE = IdentValue.createConstant(CSSConstants.CSS_GREY_VALUE);

	/**
	 * The 'honeydew' color name.
	 */
	Value HONEYDEW_VALUE = IdentValue.createConstant(CSSConstants.CSS_HONEYDEW_VALUE);

	/**
	 * The 'hotpink' color name.
	 */
	Value HOTPINK_VALUE = IdentValue.createConstant(CSSConstants.CSS_HOTPINK_VALUE);

	/**
	 * The 'indianred' color name.
	 */
	Value INDIANRED_VALUE = IdentValue.createConstant(CSSConstants.CSS_INDIANRED_VALUE);

	/**
	 * The 'indigo' color name.
	 */
	Value INDIGO_VALUE = IdentValue.createConstant(CSSConstants.CSS_INDIGO_VALUE);

	/**
	 * The 'ivory' color name.
	 */
	Value IVORY_VALUE = IdentValue.createConstant(CSSConstants.CSS_IVORY_VALUE);

	/**
	 * The 'khaki' color name.
	 */
	Value KHAKI_VALUE = IdentValue.createConstant(CSSConstants.CSS_KHAKI_VALUE);

	/**
	 * The 'lavender' color name.
	 */
	Value LAVENDER_VALUE = IdentValue.createConstant(CSSConstants.CSS_LAVENDER_VALUE);

	/**
	 * The 'lavenderblush' color name.
	 */
	Value LAVENDERBLUSH_VALUE = IdentValue.createConstant(CSSConstants.CSS_LAVENDERBLUSH_VALUE);

	/**
	 * The 'lawngreen' color name.
	 */
	Value LAWNGREEN_VALUE = IdentValue.createConstant(CSSConstants.CSS_LAWNGREEN_VALUE);

	/**
	 * The 'lemonchiffon' color name.
	 */
	Value LEMONCHIFFON_VALUE = IdentValue.createConstant(CSSConstants.CSS_LEMONCHIFFON_VALUE);

	/**
	 * The 'lightblue' color name.
	 */
	Value LIGHTBLUE_VALUE = IdentValue.createConstant(CSSConstants.CSS_LIGHTBLUE_VALUE);

	/**
	 * The 'lightcoral' color name.
	 */
	Value LIGHTCORAL_VALUE = IdentValue.createConstant(CSSConstants.CSS_LIGHTCORAL_VALUE);

	/**
	 * The 'lightcyan' color name.
	 */
	Value LIGHTCYAN_VALUE = IdentValue.createConstant(CSSConstants.CSS_LIGHTCYAN_VALUE);

	/**
	 * The 'lightgoldenrodyellow' color name.
	 */
	Value LIGHTGOLDENRODYELLOW_VALUE = IdentValue.createConstant(CSSConstants.CSS_LIGHTGOLDENRODYELLOW_VALUE);

	/**
	 * The 'lightgray' color name.
	 */
	Value LIGHTGRAY_VALUE = IdentValue.createConstant(CSSConstants.CSS_LIGHTGRAY_VALUE);

	/**
	 * The 'lightgreen' color name.
	 */
	Value LIGHTGREEN_VALUE = IdentValue.createConstant(CSSConstants.CSS_LIGHTGREEN_VALUE);

	/**
	 * The 'lightgrey' color name.
	 */
	Value LIGHTGREY_VALUE = IdentValue.createConstant(CSSConstants.CSS_LIGHTGREY_VALUE);

	/**
	 * The 'lightpink' color name.
	 */
	Value LIGHTPINK_VALUE = IdentValue.createConstant(CSSConstants.CSS_LIGHTPINK_VALUE);

	/**
	 * The 'lightsalmon' color name.
	 */
	Value LIGHTSALMON_VALUE = IdentValue.createConstant(CSSConstants.CSS_LIGHTSALMON_VALUE);

	/**
	 * The 'lightseagreen' color name.
	 */
	Value LIGHTSEAGREEN_VALUE = IdentValue.createConstant(CSSConstants.CSS_LIGHTSEAGREEN_VALUE);

	/**
	 * The 'lightskyblue' color name.
	 */
	Value LIGHTSKYBLUE_VALUE = IdentValue.createConstant(CSSConstants.CSS_LIGHTSKYBLUE_VALUE);

	/**
	 * The 'lightslategray' color name.
	 */
	Value LIGHTSLATEGRAY_VALUE = IdentValue.createConstant(CSSConstants.CSS_LIGHTSLATEGRAY_VALUE);

	/**
	 * The 'lightslategrey' color name.
	 */
	Value LIGHTSLATEGREY_VALUE = IdentValue.createConstant(CSSConstants.CSS_LIGHTSLATEGREY_VALUE);

	/**
	 * The 'lightsteelblue' color name.
	 */
	Value LIGHTSTEELBLUE_VALUE = IdentValue.createConstant(CSSConstants.CSS_LIGHTSTEELBLUE_VALUE);

	/**
	 * The 'lightyellow' color name.
	 */
	Value LIGHTYELLOW_VALUE = IdentValue.createConstant(CSSConstants.CSS_LIGHTYELLOW_VALUE);

	/**
	 * The 'limegreen' color name.
	 */
	Value LIMEGREEN_VALUE = IdentValue.createConstant(CSSConstants.CSS_LIMEGREEN_VALUE);

	/**
	 * The 'linen' color name.
	 */
	Value LINEN_VALUE = IdentValue.createConstant(CSSConstants.CSS_LINEN_VALUE);

	/**
	 * The 'magenta' color name.
	 */
	Value MAGENTA_VALUE = IdentValue.createConstant(CSSConstants.CSS_MAGENTA_VALUE);

	/**
	 * The 'mediumaquamarine' color name.
	 */
	Value MEDIUMAQUAMARINE_VALUE = IdentValue.createConstant(CSSConstants.CSS_MEDIUMAQUAMARINE_VALUE);

	/**
	 * The 'mediumblue' color name.
	 */
	Value MEDIUMBLUE_VALUE = IdentValue.createConstant(CSSConstants.CSS_MEDIUMBLUE_VALUE);

	/**
	 * The 'mediumorchid' color name.
	 */
	Value MEDIUMORCHID_VALUE = IdentValue.createConstant(CSSConstants.CSS_MEDIUMORCHID_VALUE);

	/**
	 * The 'mediumpurple' color name.
	 */
	Value MEDIUMPURPLE_VALUE = IdentValue.createConstant(CSSConstants.CSS_MEDIUMPURPLE_VALUE);

	/**
	 * The 'mediumseagreen' color name.
	 */
	Value MEDIUMSEAGREEN_VALUE = IdentValue.createConstant(CSSConstants.CSS_MEDIUMSEAGREEN_VALUE);

	/**
	 * The 'mediumslateblue' color name.
	 */
	Value MEDIUMSLATEBLUE_VALUE = IdentValue.createConstant(CSSConstants.CSS_MEDIUMSLATEBLUE_VALUE);

	/**
	 * The 'mediumspringgreen' color name.
	 */
	Value MEDIUMSPRINGGREEN_VALUE = IdentValue.createConstant(CSSConstants.CSS_MEDIUMSPRINGGREEN_VALUE);

	/**
	 * The 'mediumturquoise' color name.
	 */
	Value MEDIUMTURQUOISE_VALUE = IdentValue.createConstant(CSSConstants.CSS_MEDIUMTURQUOISE_VALUE);

	/**
	 * The 'mediumvioletred' color name.
	 */
	Value MEDIUMVIOLETRED_VALUE = IdentValue.createConstant(CSSConstants.CSS_MEDIUMVIOLETRED_VALUE);

	/**
	 * The 'midnightblue' color name.
	 */
	Value MIDNIGHTBLUE_VALUE = IdentValue.createConstant(CSSConstants.CSS_MIDNIGHTBLUE_VALUE);

	/**
	 * The 'mintcream' color name.
	 */
	Value MINTCREAM_VALUE = IdentValue.createConstant(CSSConstants.CSS_MINTCREAM_VALUE);

	/**
	 * The 'mistyrose' color name.
	 */
	Value MISTYROSE_VALUE = IdentValue.createConstant(CSSConstants.CSS_MISTYROSE_VALUE);

	/**
	 * The 'moccasin' color name.
	 */
	Value MOCCASIN_VALUE = IdentValue.createConstant(CSSConstants.CSS_MOCCASIN_VALUE);

	/**
	 * The 'navajowhite' color name.
	 */
	Value NAVAJOWHITE_VALUE = IdentValue.createConstant(CSSConstants.CSS_NAVAJOWHITE_VALUE);

	/**
	 * The 'oldlace' color name.
	 */
	Value OLDLACE_VALUE = IdentValue.createConstant(CSSConstants.CSS_OLDLACE_VALUE);

	/**
	 * The 'olivedrab' color name.
	 */
	Value OLIVEDRAB_VALUE = IdentValue.createConstant(CSSConstants.CSS_OLIVEDRAB_VALUE);

	/**
	 * The 'orange' color name.
	 */
	Value ORANGE_VALUE = IdentValue.createConstant(CSSConstants.CSS_ORANGE_VALUE);

	/**
	 * The 'orangered' color name.
	 */
	Value ORANGERED_VALUE = IdentValue.createConstant(CSSConstants.CSS_ORANGERED_VALUE);

	/**
	 * The 'orchid' color name.
	 */
	Value ORCHID_VALUE = IdentValue.createConstant(CSSConstants.CSS_ORCHID_VALUE);

	/**
	 * The 'palegoldenrod' color name.
	 */
	Value PALEGOLDENROD_VALUE = IdentValue.createConstant(CSSConstants.CSS_PALEGOLDENROD_VALUE);

	/**
	 * The 'palegreen' color name.
	 */
	Value PALEGREEN_VALUE = IdentValue.createConstant(CSSConstants.CSS_PALEGREEN_VALUE);

	/**
	 * The 'paleturquoise' color name.
	 */
	Value PALETURQUOISE_VALUE = IdentValue.createConstant(CSSConstants.CSS_PALETURQUOISE_VALUE);

	/**
	 * The 'palevioletred' color name.
	 */
	Value PALEVIOLETRED_VALUE = IdentValue.createConstant(CSSConstants.CSS_PALEVIOLETRED_VALUE);

	/**
	 * The 'papayawhip' color name.
	 */
	Value PAPAYAWHIP_VALUE = IdentValue.createConstant(CSSConstants.CSS_PAPAYAWHIP_VALUE);

	/**
	 * The 'peachpuff' color name.
	 */
	Value PEACHPUFF_VALUE = IdentValue.createConstant(CSSConstants.CSS_PEACHPUFF_VALUE);

	/**
	 * The 'peru' color name.
	 */
	Value PERU_VALUE = IdentValue.createConstant(CSSConstants.CSS_PERU_VALUE);

	/**
	 * The 'pink' color name.
	 */
	Value PINK_VALUE = IdentValue.createConstant(CSSConstants.CSS_PINK_VALUE);

	/**
	 * The 'plum' color name.
	 */
	Value PLUM_VALUE = IdentValue.createConstant(CSSConstants.CSS_PLUM_VALUE);

	/**
	 * The 'powderblue' color name.
	 */
	Value POWDERBLUE_VALUE = IdentValue.createConstant(CSSConstants.CSS_POWDERBLUE_VALUE);

	/**
	 * The 'purple' color name.
	 */
	Value PURPLE_VALUE = IdentValue.createConstant(CSSConstants.CSS_PURPLE_VALUE);

	/**
	 * The 'rosybrown' color name.
	 */
	Value ROSYBROWN_VALUE = IdentValue.createConstant(CSSConstants.CSS_ROSYBROWN_VALUE);

	/**
	 * The 'royalblue' color name.
	 */
	Value ROYALBLUE_VALUE = IdentValue.createConstant(CSSConstants.CSS_ROYALBLUE_VALUE);

	/**
	 * The 'saddlebrown' color name.
	 */
	Value SADDLEBROWN_VALUE = IdentValue.createConstant(CSSConstants.CSS_SADDLEBROWN_VALUE);

	/**
	 * The 'salmon' color name.
	 */
	Value SALMON_VALUE = IdentValue.createConstant(CSSConstants.CSS_SALMON_VALUE);

	/**
	 * The 'sandybrown' color name.
	 */
	Value SANDYBROWN_VALUE = IdentValue.createConstant(CSSConstants.CSS_SANDYBROWN_VALUE);

	/**
	 * The 'seagreen' color name.
	 */
	Value SEAGREEN_VALUE = IdentValue.createConstant(CSSConstants.CSS_SEAGREEN_VALUE);

	/**
	 * The 'seashell' color name.
	 */
	Value SEASHELL_VALUE = IdentValue.createConstant(CSSConstants.CSS_SEASHELL_VALUE);

	/**
	 * The 'sienna' color name.
	 */
	Value SIENNA_VALUE = IdentValue.createConstant(CSSConstants.CSS_SIENNA_VALUE);

	/**
	 * The 'skyblue' color name.
	 */
	Value SKYBLUE_VALUE = IdentValue.createConstant(CSSConstants.CSS_SKYBLUE_VALUE);

	/**
	 * The 'slateblue' color name.
	 */
	Value SLATEBLUE_VALUE = IdentValue.createConstant(CSSConstants.CSS_SLATEBLUE_VALUE);

	/**
	 * The 'slategray' color name.
	 */
	Value SLATEGRAY_VALUE = IdentValue.createConstant(CSSConstants.CSS_SLATEGRAY_VALUE);

	/**
	 * The 'slategrey' color name.
	 */
	Value SLATEGREY_VALUE = IdentValue.createConstant(CSSConstants.CSS_SLATEGREY_VALUE);

	/**
	 * The 'snow' color name.
	 */
	Value SNOW_VALUE = IdentValue.createConstant(CSSConstants.CSS_SNOW_VALUE);

	/**
	 * The 'springgreen' color name.
	 */
	Value SPRINGGREEN_VALUE = IdentValue.createConstant(CSSConstants.CSS_SPRINGGREEN_VALUE);

	/**
	 * The 'steelblue' color name.
	 */
	Value STEELBLUE_VALUE = IdentValue.createConstant(CSSConstants.CSS_STEELBLUE_VALUE);

	/**
	 * The 'tan' color name.
	 */
	Value TAN_VALUE = IdentValue.createConstant(CSSConstants.CSS_TAN_VALUE);

	/**
	 * The 'thistle' color name.
	 */
	Value THISTLE_VALUE = IdentValue.createConstant(CSSConstants.CSS_THISTLE_VALUE);

	/**
	 * The 'tomato' color name.
	 */
	Value TOMATO_VALUE = IdentValue.createConstant(CSSConstants.CSS_TOMATO_VALUE);

	/**
	 * The 'turquoise' color name.
	 */
	Value TURQUOISE_VALUE = IdentValue.createConstant(CSSConstants.CSS_TURQUOISE_VALUE);

	/**
	 * The 'violet' color name.
	 */
	Value VIOLET_VALUE = IdentValue.createConstant(CSSConstants.CSS_VIOLET_VALUE);

	/**
	 * The 'wheat' color name.
	 */
	Value WHEAT_VALUE = IdentValue.createConstant(CSSConstants.CSS_WHEAT_VALUE);

	/**
	 * The 'whitesmoke' color name.
	 */
	Value WHITESMOKE_VALUE = IdentValue.createConstant(CSSConstants.CSS_WHITESMOKE_VALUE);

	/**
	 * The 'yellowgreen' color name.
	 */
	Value YELLOWGREEN_VALUE = IdentValue.createConstant(CSSConstants.CSS_YELLOWGREEN_VALUE);

	/**
	 * The 'aliceblue' RGB color.
	 */
	Value ALICEBLUE_RGB_VALUE = RGBColorValue.createConstant(NUMBER_240, NUMBER_248, NUMBER_255);

	/**
	 * The 'antiquewhite' RGB color.
	 */
	Value ANTIQUEWHITE_RGB_VALUE = RGBColorValue.createConstant(NUMBER_250, NUMBER_235, NUMBER_215);

	/**
	 * The 'aquamarine' RGB color.
	 */
	Value AQUAMARINE_RGB_VALUE = RGBColorValue.createConstant(NUMBER_127, NUMBER_255, NUMBER_212);

	/**
	 * The 'azure' RGB color.
	 */
	Value AZURE_RGB_VALUE = RGBColorValue.createConstant(NUMBER_240, NUMBER_255, NUMBER_255);

	/**
	 * The 'beige' RGB color.
	 */
	Value BEIGE_RGB_VALUE = RGBColorValue.createConstant(NUMBER_245, NUMBER_245, NUMBER_220);

	/**
	 * The 'bisque' RGB color.
	 */
	Value BISQUE_RGB_VALUE = RGBColorValue.createConstant(NUMBER_255, NUMBER_228, NUMBER_196);

	/**
	 * The 'blanchedalmond' RGB color.
	 */
	Value BLANCHEDALMOND_RGB_VALUE = RGBColorValue.createConstant(NUMBER_255, NUMBER_235, NUMBER_205);

	/**
	 * The 'blueviolet' RGB color.
	 */
	Value BLUEVIOLET_RGB_VALUE = RGBColorValue.createConstant(NUMBER_138, NUMBER_43, NUMBER_226);

	/**
	 * The 'brown' RGB color.
	 */
	Value BROWN_RGB_VALUE = RGBColorValue.createConstant(NUMBER_165, NUMBER_42, NUMBER_42);

	/**
	 * The 'burlywood' RGB color.
	 */
	Value BURLYWOOD_RGB_VALUE = RGBColorValue.createConstant(NUMBER_222, NUMBER_184, NUMBER_135);

	/**
	 * The 'cadetblue' RGB color.
	 */
	Value CADETBLUE_RGB_VALUE = RGBColorValue.createConstant(NUMBER_95, NUMBER_158, NUMBER_160);

	/**
	 * The 'chartreuse' RGB color.
	 */
	Value CHARTREUSE_RGB_VALUE = RGBColorValue.createConstant(NUMBER_127, NUMBER_255, NUMBER_0);

	/**
	 * The 'chocolate' RGB color.
	 */
	Value CHOCOLATE_RGB_VALUE = RGBColorValue.createConstant(NUMBER_210, NUMBER_105, NUMBER_30);

	/**
	 * The 'coral' RGB color.
	 */
	Value CORAL_RGB_VALUE = RGBColorValue.createConstant(NUMBER_255, NUMBER_127, NUMBER_80);

	/**
	 * The 'cornflowerblue' RGB color.
	 */
	Value CORNFLOWERBLUE_RGB_VALUE = RGBColorValue.createConstant(NUMBER_100, NUMBER_149, NUMBER_237);

	/**
	 * The 'cornsilk' RGB color.
	 */
	Value CORNSILK_RGB_VALUE = RGBColorValue.createConstant(NUMBER_255, NUMBER_248, NUMBER_220);

	/**
	 * The 'crimson' RGB color.
	 */
	Value CRIMSON_RGB_VALUE = RGBColorValue.createConstant(NUMBER_220, NUMBER_20, NUMBER_60);

	/**
	 * The 'cyan' RGB color.
	 */
	Value CYAN_RGB_VALUE = RGBColorValue.createConstant(NUMBER_0, NUMBER_255, NUMBER_255);

	/**
	 * The 'darkblue' RGB color.
	 */
	Value DARKBLUE_RGB_VALUE = RGBColorValue.createConstant(NUMBER_0, NUMBER_0, NUMBER_139);

	/**
	 * The 'darkcyan' RGB color.
	 */
	Value DARKCYAN_RGB_VALUE = RGBColorValue.createConstant(NUMBER_0, NUMBER_139, NUMBER_139);

	/**
	 * The 'darkgoldenrod' RGB color.
	 */
	Value DARKGOLDENROD_RGB_VALUE = RGBColorValue.createConstant(NUMBER_184, NUMBER_134, NUMBER_11);

	/**
	 * The 'darkgray' RGB color.
	 */
	Value DARKGRAY_RGB_VALUE = RGBColorValue.createConstant(NUMBER_169, NUMBER_169, NUMBER_169);

	/**
	 * The 'darkgreen' RGB color.
	 */
	Value DARKGREEN_RGB_VALUE = RGBColorValue.createConstant(NUMBER_0, NUMBER_100, NUMBER_0);

	/**
	 * The 'darkgrey' RGB color.
	 */
	Value DARKGREY_RGB_VALUE = RGBColorValue.createConstant(NUMBER_169, NUMBER_169, NUMBER_169);

	/**
	 * The 'darkkhaki' RGB color.
	 */
	Value DARKKHAKI_RGB_VALUE = RGBColorValue.createConstant(NUMBER_189, NUMBER_183, NUMBER_107);

	/**
	 * The 'darkmagenta' RGB color.
	 */
	Value DARKMAGENTA_RGB_VALUE = RGBColorValue.createConstant(NUMBER_139, NUMBER_0, NUMBER_139);

	/**
	 * The 'darkolivegreen' RGB color.
	 */
	Value DARKOLIVEGREEN_RGB_VALUE = RGBColorValue.createConstant(NUMBER_85, NUMBER_107, NUMBER_47);

	/**
	 * The 'darkorange' RGB color.
	 */
	Value DARKORANGE_RGB_VALUE = RGBColorValue.createConstant(NUMBER_255, NUMBER_140, NUMBER_0);

	/**
	 * The 'darkorchid' RGB color.
	 */
	Value DARKORCHID_RGB_VALUE = RGBColorValue.createConstant(NUMBER_153, NUMBER_50, NUMBER_204);

	/**
	 * The 'darkred' RGB color.
	 */
	Value DARKRED_RGB_VALUE = RGBColorValue.createConstant(NUMBER_139, NUMBER_0, NUMBER_0);

	/**
	 * The 'darksalmon' RGB color.
	 */
	Value DARKSALMON_RGB_VALUE = RGBColorValue.createConstant(NUMBER_233, NUMBER_150, NUMBER_122);

	/**
	 * The 'darkseagreen' RGB color.
	 */
	Value DARKSEAGREEN_RGB_VALUE = RGBColorValue.createConstant(NUMBER_143, NUMBER_188, NUMBER_143);

	/**
	 * The 'darkslateblue' RGB color.
	 */
	Value DARKSLATEBLUE_RGB_VALUE = RGBColorValue.createConstant(NUMBER_72, NUMBER_61, NUMBER_139);

	/**
	 * The 'darkslategray' RGB color.
	 */
	Value DARKSLATEGRAY_RGB_VALUE = RGBColorValue.createConstant(NUMBER_47, NUMBER_79, NUMBER_79);

	/**
	 * The 'darkslategrey' RGB color.
	 */
	Value DARKSLATEGREY_RGB_VALUE = RGBColorValue.createConstant(NUMBER_47, NUMBER_79, NUMBER_79);

	/**
	 * The 'darkturquoise' RGB color.
	 */
	Value DARKTURQUOISE_RGB_VALUE = RGBColorValue.createConstant(NUMBER_0, NUMBER_206, NUMBER_209);

	/**
	 * The 'darkviolet' RGB color.
	 */
	Value DARKVIOLET_RGB_VALUE = RGBColorValue.createConstant(NUMBER_148, NUMBER_0, NUMBER_211);

	/**
	 * The 'deeppink' RGB color.
	 */
	Value DEEPPINK_RGB_VALUE = RGBColorValue.createConstant(NUMBER_255, NUMBER_20, NUMBER_147);

	/**
	 * The 'deepskyblue' RGB color.
	 */
	Value DEEPSKYBLUE_RGB_VALUE = RGBColorValue.createConstant(NUMBER_0, NUMBER_191, NUMBER_255);

	/**
	 * The 'dimgray' RGB color.
	 */
	Value DIMGRAY_RGB_VALUE = RGBColorValue.createConstant(NUMBER_105, NUMBER_105, NUMBER_105);

	/**
	 * The 'dimgrey' RGB color.
	 */
	Value DIMGREY_RGB_VALUE = RGBColorValue.createConstant(NUMBER_105, NUMBER_105, NUMBER_105);

	/**
	 * The 'dodgerblue' RGB color.
	 */
	Value DODGERBLUE_RGB_VALUE = RGBColorValue.createConstant(NUMBER_30, NUMBER_144, NUMBER_255);

	/**
	 * The 'firebrick' RGB color.
	 */
	Value FIREBRICK_RGB_VALUE = RGBColorValue.createConstant(NUMBER_178, NUMBER_34, NUMBER_34);

	/**
	 * The 'floralwhite' RGB color.
	 */
	Value FLORALWHITE_RGB_VALUE = RGBColorValue.createConstant(NUMBER_255, NUMBER_250, NUMBER_240);

	/**
	 * The 'forestgreen' RGB color.
	 */
	Value FORESTGREEN_RGB_VALUE = RGBColorValue.createConstant(NUMBER_34, NUMBER_139, NUMBER_34);

	/**
	 * The 'gainsboro' RGB color.
	 */
	Value GAINSBORO_RGB_VALUE = RGBColorValue.createConstant(NUMBER_220, NUMBER_200, NUMBER_200);

	/**
	 * The 'ghostwhite' RGB color.
	 */
	Value GHOSTWHITE_RGB_VALUE = RGBColorValue.createConstant(NUMBER_248, NUMBER_248, NUMBER_255);

	/**
	 * The 'gold' RGB color.
	 */
	Value GOLD_RGB_VALUE = RGBColorValue.createConstant(NUMBER_255, NUMBER_215, NUMBER_0);

	/**
	 * The 'goldenrod' RGB color.
	 */
	Value GOLDENROD_RGB_VALUE = RGBColorValue.createConstant(NUMBER_218, NUMBER_165, NUMBER_32);

	/**
	 * The 'grey' RGB color.
	 */
	Value GREY_RGB_VALUE = RGBColorValue.createConstant(NUMBER_128, NUMBER_128, NUMBER_128);

	/**
	 * The 'greenyellow' RGB color.
	 */
	Value GREENYELLOW_RGB_VALUE = RGBColorValue.createConstant(NUMBER_173, NUMBER_255, NUMBER_47);

	/**
	 * The 'honeydew' RGB color.
	 */
	Value HONEYDEW_RGB_VALUE = RGBColorValue.createConstant(NUMBER_240, NUMBER_255, NUMBER_240);

	/**
	 * The 'hotpink' RGB color.
	 */
	Value HOTPINK_RGB_VALUE = RGBColorValue.createConstant(NUMBER_255, NUMBER_105, NUMBER_180);

	/**
	 * The 'indianred' RGB color.
	 */
	Value INDIANRED_RGB_VALUE = RGBColorValue.createConstant(NUMBER_205, NUMBER_92, NUMBER_92);

	/**
	 * The 'indigo' RGB color.
	 */
	Value INDIGO_RGB_VALUE = RGBColorValue.createConstant(NUMBER_75, NUMBER_0, NUMBER_130);

	/**
	 * The 'ivory' RGB color.
	 */
	Value IVORY_RGB_VALUE = RGBColorValue.createConstant(NUMBER_255, NUMBER_255, NUMBER_240);

	/**
	 * The 'khaki' RGB color.
	 */
	Value KHAKI_RGB_VALUE = RGBColorValue.createConstant(NUMBER_240, NUMBER_230, NUMBER_140);

	/**
	 * The 'lavender' RGB color.
	 */
	Value LAVENDER_RGB_VALUE = RGBColorValue.createConstant(NUMBER_230, NUMBER_230, NUMBER_250);

	/**
	 * The 'lavenderblush' RGB color.
	 */
	Value LAVENDERBLUSH_RGB_VALUE = RGBColorValue.createConstant(NUMBER_255, NUMBER_240, NUMBER_255);

	/**
	 * The 'lawngreen' RGB color.
	 */
	Value LAWNGREEN_RGB_VALUE = RGBColorValue.createConstant(NUMBER_124, NUMBER_252, NUMBER_0);

	/**
	 * The 'lemonchiffon' RGB color.
	 */
	Value LEMONCHIFFON_RGB_VALUE = RGBColorValue.createConstant(NUMBER_255, NUMBER_250, NUMBER_205);

	/**
	 * The 'lightblue' RGB color.
	 */
	Value LIGHTBLUE_RGB_VALUE = RGBColorValue.createConstant(NUMBER_173, NUMBER_216, NUMBER_230);

	/**
	 * The 'lightcoral' RGB color.
	 */
	Value LIGHTCORAL_RGB_VALUE = RGBColorValue.createConstant(NUMBER_240, NUMBER_128, NUMBER_128);

	/**
	 * The 'lightcyan' RGB color.
	 */
	Value LIGHTCYAN_RGB_VALUE = RGBColorValue.createConstant(NUMBER_224, NUMBER_255, NUMBER_255);

	/**
	 * The 'lightgoldenrodyellow' RGB color.
	 */
	Value LIGHTGOLDENRODYELLOW_RGB_VALUE = RGBColorValue.createConstant(NUMBER_250, NUMBER_250, NUMBER_210);

	/**
	 * The 'lightgray' RGB color.
	 */
	Value LIGHTGRAY_RGB_VALUE = RGBColorValue.createConstant(NUMBER_211, NUMBER_211, NUMBER_211);

	/**
	 * The 'lightgreen' RGB color.
	 */
	Value LIGHTGREEN_RGB_VALUE = RGBColorValue.createConstant(NUMBER_144, NUMBER_238, NUMBER_144);

	/**
	 * The 'lightgrey' RGB color.
	 */
	Value LIGHTGREY_RGB_VALUE = RGBColorValue.createConstant(NUMBER_211, NUMBER_211, NUMBER_211);

	/**
	 * The 'lightpink' RGB color.
	 */
	Value LIGHTPINK_RGB_VALUE = RGBColorValue.createConstant(NUMBER_255, NUMBER_182, NUMBER_193);

	/**
	 * The 'lightsalmon' RGB color.
	 */
	Value LIGHTSALMON_RGB_VALUE = RGBColorValue.createConstant(NUMBER_255, NUMBER_160, NUMBER_122);

	/**
	 * The 'lightseagreen' RGB color.
	 */
	Value LIGHTSEAGREEN_RGB_VALUE = RGBColorValue.createConstant(NUMBER_32, NUMBER_178, NUMBER_170);

	/**
	 * The 'lightskyblue' RGB color.
	 */
	Value LIGHTSKYBLUE_RGB_VALUE = RGBColorValue.createConstant(NUMBER_135, NUMBER_206, NUMBER_250);

	/**
	 * The 'lightslategray' RGB color.
	 */
	Value LIGHTSLATEGRAY_RGB_VALUE = RGBColorValue.createConstant(NUMBER_119, NUMBER_136, NUMBER_153);

	/**
	 * The 'lightslategrey' RGB color.
	 */
	Value LIGHTSLATEGREY_RGB_VALUE = RGBColorValue.createConstant(NUMBER_119, NUMBER_136, NUMBER_153);

	/**
	 * The 'lightsteelblue' RGB color.
	 */
	Value LIGHTSTEELBLUE_RGB_VALUE = RGBColorValue.createConstant(NUMBER_176, NUMBER_196, NUMBER_222);

	/**
	 * The 'lightyellow' RGB color.
	 */
	Value LIGHTYELLOW_RGB_VALUE = RGBColorValue.createConstant(NUMBER_255, NUMBER_255, NUMBER_224);

	/**
	 * The 'limegreen' RGB color.
	 */
	Value LIMEGREEN_RGB_VALUE = RGBColorValue.createConstant(NUMBER_50, NUMBER_205, NUMBER_50);

	/**
	 * The 'linen' RGB color.
	 */
	Value LINEN_RGB_VALUE = RGBColorValue.createConstant(NUMBER_250, NUMBER_240, NUMBER_230);

	/**
	 * The 'magenta' RGB color.
	 */
	Value MAGENTA_RGB_VALUE = RGBColorValue.createConstant(NUMBER_255, NUMBER_0, NUMBER_255);

	/**
	 * The 'mediumaquamarine' RGB color.
	 */
	Value MEDIUMAQUAMARINE_RGB_VALUE = RGBColorValue.createConstant(NUMBER_102, NUMBER_205, NUMBER_170);

	/**
	 * The 'mediumblue' RGB color.
	 */
	Value MEDIUMBLUE_RGB_VALUE = RGBColorValue.createConstant(NUMBER_0, NUMBER_0, NUMBER_205);

	/**
	 * The 'mediumorchid' RGB color.
	 */
	Value MEDIUMORCHID_RGB_VALUE = RGBColorValue.createConstant(NUMBER_186, NUMBER_85, NUMBER_211);

	/**
	 * The 'mediumpurple' RGB color.
	 */
	Value MEDIUMPURPLE_RGB_VALUE = RGBColorValue.createConstant(NUMBER_147, NUMBER_112, NUMBER_219);

	/**
	 * The 'mediumseagreen' RGB color.
	 */
	Value MEDIUMSEAGREEN_RGB_VALUE = RGBColorValue.createConstant(NUMBER_60, NUMBER_179, NUMBER_113);

	/**
	 * The 'mediumslateblue' RGB color.
	 */
	Value MEDIUMSLATEBLUE_RGB_VALUE = RGBColorValue.createConstant(NUMBER_123, NUMBER_104, NUMBER_238);

	/**
	 * The 'mediumspringgreen' RGB color.
	 */
	Value MEDIUMSPRINGGREEN_RGB_VALUE = RGBColorValue.createConstant(NUMBER_0, NUMBER_250, NUMBER_154);

	/**
	 * The 'mediumturquoise' RGB color.
	 */
	Value MEDIUMTURQUOISE_RGB_VALUE = RGBColorValue.createConstant(NUMBER_72, NUMBER_209, NUMBER_204);

	/**
	 * The 'mediumvioletred' RGB color.
	 */
	Value MEDIUMVIOLETRED_RGB_VALUE = RGBColorValue.createConstant(NUMBER_199, NUMBER_21, NUMBER_133);

	/**
	 * The 'midnightblue' RGB color.
	 */
	Value MIDNIGHTBLUE_RGB_VALUE = RGBColorValue.createConstant(NUMBER_25, NUMBER_25, NUMBER_112);

	/**
	 * The 'mintcream' RGB color.
	 */
	Value MINTCREAM_RGB_VALUE = RGBColorValue.createConstant(NUMBER_245, NUMBER_255, NUMBER_250);

	/**
	 * The 'mistyrose' RGB color.
	 */
	Value MISTYROSE_RGB_VALUE = RGBColorValue.createConstant(NUMBER_255, NUMBER_228, NUMBER_225);

	/**
	 * The 'moccasin' RGB color.
	 */
	Value MOCCASIN_RGB_VALUE = RGBColorValue.createConstant(NUMBER_255, NUMBER_228, NUMBER_181);

	/**
	 * The 'navajowhite' RGB color.
	 */
	Value NAVAJOWHITE_RGB_VALUE = RGBColorValue.createConstant(NUMBER_255, NUMBER_222, NUMBER_173);

	/**
	 * The 'oldlace' RGB color.
	 */
	Value OLDLACE_RGB_VALUE = RGBColorValue.createConstant(NUMBER_253, NUMBER_245, NUMBER_230);

	/**
	 * The 'olivedrab' RGB color.
	 */
	Value OLIVEDRAB_RGB_VALUE = RGBColorValue.createConstant(NUMBER_107, NUMBER_142, NUMBER_35);

	/**
	 * The 'orange' RGB color.
	 */
	Value ORANGE_RGB_VALUE = RGBColorValue.createConstant(NUMBER_255, NUMBER_165, NUMBER_0);

	/**
	 * The 'orangered' RGB color.
	 */
	Value ORANGERED_RGB_VALUE = RGBColorValue.createConstant(NUMBER_255, NUMBER_69, NUMBER_0);

	/**
	 * The 'orchid' RGB color.
	 */
	Value ORCHID_RGB_VALUE = RGBColorValue.createConstant(NUMBER_218, NUMBER_112, NUMBER_214);

	/**
	 * The 'palegoldenrod' RGB color.
	 */
	Value PALEGOLDENROD_RGB_VALUE = RGBColorValue.createConstant(NUMBER_238, NUMBER_232, NUMBER_170);

	/**
	 * The 'palegreen' RGB color.
	 */
	Value PALEGREEN_RGB_VALUE = RGBColorValue.createConstant(NUMBER_152, NUMBER_251, NUMBER_152);

	/**
	 * The 'paleturquoise' RGB color.
	 */
	Value PALETURQUOISE_RGB_VALUE = RGBColorValue.createConstant(NUMBER_175, NUMBER_238, NUMBER_238);

	/**
	 * The 'palevioletred' RGB color.
	 */
	Value PALEVIOLETRED_RGB_VALUE = RGBColorValue.createConstant(NUMBER_219, NUMBER_112, NUMBER_147);

	/**
	 * The 'papayawhip' RGB color.
	 */
	Value PAPAYAWHIP_RGB_VALUE = RGBColorValue.createConstant(NUMBER_255, NUMBER_239, NUMBER_213);

	/**
	 * The 'peachpuff' RGB color.
	 */
	Value PEACHPUFF_RGB_VALUE = RGBColorValue.createConstant(NUMBER_255, NUMBER_218, NUMBER_185);

	/**
	 * The 'peru' RGB color.
	 */
	Value PERU_RGB_VALUE = RGBColorValue.createConstant(NUMBER_205, NUMBER_133, NUMBER_63);

	/**
	 * The 'pink' RGB color.
	 */
	Value PINK_RGB_VALUE = RGBColorValue.createConstant(NUMBER_255, NUMBER_192, NUMBER_203);

	/**
	 * The 'plum' RGB color.
	 */
	Value PLUM_RGB_VALUE = RGBColorValue.createConstant(NUMBER_221, NUMBER_160, NUMBER_221);

	/**
	 * The 'powderblue' RGB color.
	 */
	Value POWDERBLUE_RGB_VALUE = RGBColorValue.createConstant(NUMBER_176, NUMBER_224, NUMBER_230);

	/**
	 * The 'rosybrown' RGB color.
	 */
	Value ROSYBROWN_RGB_VALUE = RGBColorValue.createConstant(NUMBER_188, NUMBER_143, NUMBER_143);

	/**
	 * The 'royalblue' RGB color.
	 */
	Value ROYALBLUE_RGB_VALUE = RGBColorValue.createConstant(NUMBER_65, NUMBER_105, NUMBER_225);

	/**
	 * The 'saddlebrown' RGB color.
	 */
	Value SADDLEBROWN_RGB_VALUE = RGBColorValue.createConstant(NUMBER_139, NUMBER_69, NUMBER_19);

	/**
	 * The 'salmon' RGB color.
	 */
	Value SALMON_RGB_VALUE = RGBColorValue.createConstant(NUMBER_250, NUMBER_69, NUMBER_114);

	/**
	 * The 'sandybrown' RGB color.
	 */
	Value SANDYBROWN_RGB_VALUE = RGBColorValue.createConstant(NUMBER_244, NUMBER_164, NUMBER_96);

	/**
	 * The 'seagreen' RGB color.
	 */
	Value SEAGREEN_RGB_VALUE = RGBColorValue.createConstant(NUMBER_46, NUMBER_139, NUMBER_87);

	/**
	 * The 'seashell' RGB color.
	 */
	Value SEASHELL_RGB_VALUE = RGBColorValue.createConstant(NUMBER_255, NUMBER_245, NUMBER_238);

	/**
	 * The 'sienna' RGB color.
	 */
	Value SIENNA_RGB_VALUE = RGBColorValue.createConstant(NUMBER_160, NUMBER_82, NUMBER_45);

	/**
	 * The 'skyblue' RGB color.
	 */
	Value SKYBLUE_RGB_VALUE = RGBColorValue.createConstant(NUMBER_135, NUMBER_206, NUMBER_235);

	/**
	 * The 'slateblue' RGB color.
	 */
	Value SLATEBLUE_RGB_VALUE = RGBColorValue.createConstant(NUMBER_106, NUMBER_90, NUMBER_205);

	/**
	 * The 'slategray' RGB color.
	 */
	Value SLATEGRAY_RGB_VALUE = RGBColorValue.createConstant(NUMBER_112, NUMBER_128, NUMBER_144);

	/**
	 * The 'slategrey' RGB color.
	 */
	Value SLATEGREY_RGB_VALUE = RGBColorValue.createConstant(NUMBER_112, NUMBER_128, NUMBER_144);

	/**
	 * The 'snow' RGB color.
	 */
	Value SNOW_RGB_VALUE = RGBColorValue.createConstant(NUMBER_255, NUMBER_250, NUMBER_250);

	/**
	 * The 'springgreen' RGB color.
	 */
	Value SPRINGGREEN_RGB_VALUE = RGBColorValue.createConstant(NUMBER_0, NUMBER_255, NUMBER_127);

	/**
	 * The 'steelblue' RGB color.
	 */
	Value STEELBLUE_RGB_VALUE = RGBColorValue.createConstant(NUMBER_70, NUMBER_130, NUMBER_180);

	/**
	 * The 'tan' RGB color.
	 */
	Value TAN_RGB_VALUE = RGBColorValue.createConstant(NUMBER_210, NUMBER_180, NUMBER_140);

	/**
	 * The 'thistle' RGB color.
	 */
	Value THISTLE_RGB_VALUE = RGBColorValue.createConstant(NUMBER_216, NUMBER_91, NUMBER_216);

	/**
	 * The 'tomato' RGB color.
	 */
	Value TOMATO_RGB_VALUE = RGBColorValue.createConstant(NUMBER_255, NUMBER_99, NUMBER_71);

	/**
	 * The 'turquoise' RGB color.
	 */
	Value TURQUOISE_RGB_VALUE = RGBColorValue.createConstant(NUMBER_64, NUMBER_224, NUMBER_208);

	/**
	 * The 'violet' RGB color.
	 */
	Value VIOLET_RGB_VALUE = RGBColorValue.createConstant(NUMBER_238, NUMBER_130, NUMBER_238);

	/**
	 * The 'wheat' RGB color.
	 */
	Value WHEAT_RGB_VALUE = RGBColorValue.createConstant(NUMBER_245, NUMBER_222, NUMBER_179);

	/**
	 * The 'whitesmoke' RGB color.
	 */
	Value WHITESMOKE_RGB_VALUE = RGBColorValue.createConstant(NUMBER_245, NUMBER_245, NUMBER_245);

	/**
	 * The 'yellowgreen' RGB color.
	 */
	Value YELLOWGREEN_RGB_VALUE = RGBColorValue.createConstant(NUMBER_154, NUMBER_205, NUMBER_50);

}
