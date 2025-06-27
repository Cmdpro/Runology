#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D SpecialBypassSampler;

in vec2 texCoord;
in vec2 oneTexel;
out vec4 fragColor;

void main() {
    vec4 color = texture(SpecialBypassSampler, texCoord);
    fragColor = vec4(mix(texture(DiffuseSampler, texCoord).rgb, vec3(color.rgb), color.a), 1.0);
}