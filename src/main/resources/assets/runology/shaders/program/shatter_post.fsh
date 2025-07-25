#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D ShatterSampler;
uniform sampler2D ShatterDepthSampler;

uniform mat4 invViewMat;
uniform mat4 invProjMat;
uniform vec3 CameraPosition;

in vec2 texCoord;
in vec2 oneTexel;
out vec4 fragColor;

const float near = 1.0;
const float far = 100.0;

float linearizeDepth(float depth) {
    return (2.0 * near) / (far + near - depth * (far - near));
}

vec4 sobel(bool depth) {
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
        if (depth) {
            float sampleVar = linearizeDepth(texture(ShatterDepthSampler, coord).r);
            intensity = sampleVar;
        } else {
            vec3 sampleVar = texture(ShatterSampler, coord).rgb;
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
    float edgeThreshold = 0.2;
    float alpha = G > edgeThreshold ? 1.0 : 0.0;
    return vec4(G, G, G, alpha);
}
void main() {
    vec3 color = vec3(0.0, 1.0, 0.0);
    float depthAlpha = 0.0;
    if (texture(ShatterSampler, texCoord).r < 0.5) {
        depthAlpha = sobel(true).a;
    }
    float blend = max(sobel(false).a, depthAlpha);
    fragColor = vec4(mix(texture(DiffuseSampler, texCoord).rgb, vec3(color.rgb), blend), 1.0);
}