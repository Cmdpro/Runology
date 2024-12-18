#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D ShatterSampler;

in vec2 texCoord;
in vec2 oneTexel;
out vec4 fragColor;

const float near = 1.0;
const float far = 100.0;

vec4 sobel() {
    float kernel[9] = float[](1, 2, 1, 0, 0, 0, -1, -2, -1);
    vec2 offset[9] = vec2[](
    vec2(-oneTexel.x, oneTexel.y),
    vec2(0, oneTexel.y),
    vec2(oneTexel.x, oneTexel.y),
    vec2(-oneTexel.x, 0),
    vec2(0, 0),
    vec2(oneTexel.x, 0),
    vec2(-oneTexel.x, -oneTexel.y),
    vec2(0, -oneTexel.y),
    vec2(oneTexel.x, -oneTexel.y)
    );

    float Gx = 0.0;
    float Gy = 0.0;
    for (int i = 0; i < 9; i++) {
        float intensity;
        vec2 coord = texCoord + offset[i];
        coord.x = clamp(coord.x, 0, 1);
        coord.y = clamp(coord.y, 0, 1);
        vec3 sampleVar = texture(ShatterSampler, coord).rgb;
        intensity = (sampleVar.r + sampleVar.g + sampleVar.b) / 3.0;

        if (i != 4) {
            Gx += intensity * kernel[i];
        }
        int j = (i % 3) * 3 + i / 3;
        if (j != 4) {
            Gy += intensity * kernel[j];
        }
    }
    float G = sqrt(Gx * Gx + Gy * Gy);
    float edgeThreshold = 0.2;
    float alpha = G > edgeThreshold ? 1.0 : 0.0;
    return vec4(G, G, G, alpha);
}
void main() {
    vec3 color = vec3(0.0, 1.0, 0.0);
    float blend = sobel().a;
    fragColor = mix(texture2D(DiffuseSampler, texCoord), vec4(color, 1), blend);
}