#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D SpecialBypassSampler;

in vec2 texCoord;
in vec2 oneTexel;
out vec4 fragColor;

void main() {
    vec4 color = texture(SpecialBypassSampler, texCoord);
    fragColor = mix(texture(DiffuseSampler, texCoord), vec4(color.rgb, 1.0), color.a);
    //fragColor = vec4(mix(color.rgb, vec3(1.0, 0.0, 1.0), color.a), 1.0);
}