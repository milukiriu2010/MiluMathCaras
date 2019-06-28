#version 300 es
// --------------------------------------
// 放射状に広がる線を描く
// --------------------------------------
// 2019.06.28
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

void main() {
    // 0～1の範囲で入ってくるマウスの位置を
    // -1～1の範囲に正規化している
    // Y座標は上下逆のため、正負を逆転している
    vec2 m = vec2(u_mouse.x*2.0-1.0,-u_mouse.y*2.0+1.0);
    // 今から処理しようとしているスクリーン上のピクセル位置を
    // -1～1の範囲に正規化している
    vec2 p = (gl_FragCoord.xy * 2.0 - u_resolution)/min(u_resolution.x, u_resolution.y);

    float tt = atan(p.y-m.y,p.x-m.x) + u_time;
    tt = sin(tt*10.0);

    o_FragColor = vec4(vec3(tt), 1.0);
}
