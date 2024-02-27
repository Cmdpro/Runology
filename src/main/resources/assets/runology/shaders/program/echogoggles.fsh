#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D DiffuseDepthSampler;

in vec2 texCoord;
in vec2 oneTexel;
out vec4 fragColor;

const float near = 1.0;
const float far = 100.0;

float linearizeDepth(float depth) {
    return (2.0 * near) / (far + near - depth * (far - near));
}
vec4 sobel(sampler2D s, vec3 color, bool isDepth) {
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
        if (isDepth) {
            float depth = texture(s, texCoord + offset[i]).r;
            intensity = linearizeDepth(depth);
        } else {
            vec3 sampleVar = texture(s, texCoord + offset[i]).rgb;
            intensity = (sampleVar.r + sampleVar.g + sampleVar.b) / 3.0;
        }

        if (i != 4) {
            Gx += intensity * kernel[i];
        }
        int j = (i % 3) * 3 + i / 3;
        if (j != 4) {
            Gy += intensity * kernel[j];
        }
    }
    float G = sqrt(Gx * Gx + Gy * Gy);
    float edgeThreshold = 0.1; // Dont set this to 0.0, or do.
    float alpha = G > edgeThreshold ? 1.0 : 0.0;
    return vec4(G * color, alpha);
}
void main() {
    vec4 sobelColor = sobel(DiffuseSampler, vec3(0.5, 0.5, 1.0), false);
    vec4 sobelDepth = sobel(DiffuseDepthSampler, vec3(0.5, 0.5, 1.0), true);
    vec4 sobelMix = mix(sobelColor, sobelDepth, 0.70);
    fragColor = sobelMix;
}