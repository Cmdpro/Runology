#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D DiffuseDepthSampler;
uniform sampler2D ShatteredSkySampler;
uniform sampler2D ShatteredSkyBgSampler;
uniform float FogStart;
uniform float FogEnd;
uniform mat4 invViewMat;
uniform mat4 invProjMat;
uniform vec3 CameraPosition;

in vec2 texCoord;
in vec2 oneTexel;
out vec4 fragColor;

vec4 linear_fog(vec4 inColor, float vertexDistance, float fogStart, float fogEnd, vec4 fogColor) {
    if (vertexDistance <= fogStart) {
        return inColor;
    }

    float fogValue = vertexDistance < fogEnd ? smoothstep(fogStart, fogEnd, vertexDistance) : 1.0;
    return vec4(mix(inColor.rgb, fogColor.rgb, fogValue * fogColor.a), inColor.a);
}
vec3 worldPos(float depth) {
    float z = depth * 2.0 - 1.0;
    vec4 clipSpacePosition = vec4(texCoord * 2.0 - 1.0, z, 1.0);
    vec4 viewSpacePosition = invProjMat * clipSpacePosition;
    viewSpacePosition /= viewSpacePosition.w;
    vec4 worldSpacePosition = invViewMat * viewSpacePosition;

    return CameraPosition + worldSpacePosition.xyz;
}
void main() {
    if (texture(ShatteredSkySampler, texCoord).r <= 0.5) {
        vec3 color = texture(ShatteredSkyBgSampler, texCoord).rgb;
        vec3 world = worldPos(texture(DiffuseDepthSampler, texCoord).r);
        float dist = distance(world, CameraPosition);
        fragColor = linear_fog(mix(texture(DiffuseSampler, texCoord), vec4(0.0, 0.0, 0.0, 1.0), 0.2), dist, FogStart, FogEnd, vec4(color, 1));
    } else {
        fragColor = texture(DiffuseSampler, texCoord);
    }
}