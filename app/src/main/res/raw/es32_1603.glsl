#version 300 es
// --------------------------------------
// 10個の円が競争する
// --------------------------------------
// 2019.07.06
// --------------------------------------

precision highp   float;

// 経過時間(ミリ秒を1/1000)
uniform   float     u_time;
// タッチ位置
// 0.0-1.0に正規化
uniform   vec2      u_mouse;
// 描画領域の幅・高さ
uniform   vec2      u_resolution;

out vec4 o_FragColor;

const vec4 red = vec4(1.0,0.0,0.0,1.0);
const float PI5 = 0.628318;

void main() {
    // 0～1の範囲で入ってくるマウスの位置を
    // -1～1の範囲に正規化している
    // Y座標は上下逆のため、正負を逆転している
    vec2 m = vec2(u_mouse.x*2.0-1.0,-u_mouse.y*2.0+1.0);
    // 今から処理しようとしているスクリーン上のピクセル位置を
    // -1～1の範囲に正規化している
    vec2 p = (gl_FragCoord.xy * 2.0 - u_resolution)/min(u_resolution.x, u_resolution.y);
	float t = u_time;

	vec4 smpColor = vec4(1.0,1.0,1.0,1.0);
	float f = 0.0;
	for (float i=0.0; i < 10.0; i++) {
		float s = sin(i*PI5*t) * 0.5;
		float c = cos(i*PI5*t) * 0.5;
		f += 0.001/abs(length(p+vec2(c,s)-m)-0.5);
	}

	if (f > 0.1) {
		o_FragColor = smpColor*red*f;
	}
	else {
		o_FragColor = vec4(1.0,1.0,1.0,1.0);
	}
}
