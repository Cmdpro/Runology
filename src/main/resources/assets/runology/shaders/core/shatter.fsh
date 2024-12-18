#version 150

#moj_import <matrix.glsl>

uniform sampler2D Sampler0;

uniform float GameTime;
uniform int EndPortalLayers;

in vec4 texProj0;

out vec4 fragColor;

void main() {
    fragColor = vec4(vec3(0.0, 0.0, 0.0), 1.0);
}
