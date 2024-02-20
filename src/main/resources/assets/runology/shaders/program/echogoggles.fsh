#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D MainDepthSampler;

in vec2 texCoord;
out vec4 fragColor;

float linearizeDepth(float depth) {
    float n = 1.0;
    float f = 100.0;
    return (2.0 * n) / (f + n - depth * (f - n));
}
void main() {
    float mainDepth = texture(MainDepthSampler, texCoord).r;
    float depth = linearizeDepth(mainDepth);
    fragColor = texture(MainDepthSampler, texCoord);
}
