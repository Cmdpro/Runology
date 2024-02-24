#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D MainDepthSampler;

varying vec2 texCoord;

float near = 0.1;
float far = 1000.0;

float linearizeDepth(float depth) {
    float z = depth * 2.0 - 1.0;
    return (near * far) / (far + near - z * (far - near));
}
void main() {
    float depth = linearizeDepth(texture2D(MainDepthSampler, texCoord).r);
    vec4 color = texture2D(DiffuseSampler, texCoord);
    gl_FragColor = vec4(color.rgb/depth, color.a);
}