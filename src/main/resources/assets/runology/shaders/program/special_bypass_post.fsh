#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D SpecialBypassSampler;
uniform sampler2D SpecialBypassDepthSampler;
uniform sampler2D ShatterDepthSampler;
uniform sampler2D PlayerPowerDepthSampler;

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

void main() {
    vec4 color = texture(SpecialBypassSampler, texCoord);
    float closest = min(linearizeDepth(texture(ShatterDepthSampler, texCoord).r), linearizeDepth(texture(PlayerPowerDepthSampler, texCoord).r));
    float depth = linearizeDepth(texture(SpecialBypassDepthSampler, texCoord).r);
    if (depth > closest) {
        if (distance(depth, closest) > 0.05) {
            color.a = 0.0;
        }
    }
    fragColor = vec4(mix(texture(DiffuseSampler, texCoord).rgb, vec3(color.rgb), color.a), 1.0);
}