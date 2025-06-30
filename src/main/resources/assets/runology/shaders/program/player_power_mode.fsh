#version 150

uniform sampler2D DiffuseSampler;

uniform float time;

in vec2 texCoord;
in vec2 oneTexel;
out vec4 fragColor;

bool pointInTriangle(vec2 s, vec2 a, vec2 b, vec2 c)
{
    float as_x = s.x - a.x;
    float as_y = s.y - a.y;

    bool s_ab = (b.x - a.x) * as_y - (b.y - a.y) * as_x > 0;

    if ((c.x - a.x) * as_y - (c.y - a.y) * as_x > 0 == s_ab) {
        return false;
    }
    if ((c.x - b.x) * (s.y - b.y) - (c.y - b.y)*(s.x - b.x) > 0 != s_ab) {
        return false;
    }
    return true;
}

vec4 getColorForPos(vec3 color, vec3 outlineColor) {
    vec2 change = normalize(oneTexel)*0.002;
    vec2 offsets[8] = vec2[](
    vec2(-change.x, change.y),
    vec2(0, change.y),
    vec2(change.x, change.y),
    vec2(-change.x, 0),
    vec2(change.x, 0),
    vec2(-change.x, -change.y),
    vec2(0, -change.y),
    vec2(change.x, -change.y)
    );

    vec2[24] triangles1;
    vec2[24] triangles2;
    vec2[24] triangles3;
    vec2 pos = vec2(-0.1, 0.0);
    vec2 offset = vec2(0.0, 1.0/5.0);
    for (int i = 0; i < 6; i++) {
        vec2 target = vec2(0.5, 0.5);
        target += normalize(pos-target)*(0.4);
        triangles1[i] = vec2(pos-offset);
        triangles2[i] = vec2(pos+offset);
        triangles3[i] = target;
        pos += offset;
    }
    pos = vec2(0.0, 1.1);
    offset = vec2(1.0/5.0, 0.0);
    for (int i = 6; i < 12; i++) {
        vec2 target = vec2(0.5, 0.5);
        target += normalize(pos-target)*(0.4);
        triangles1[i] = vec2(pos-offset);
        triangles2[i] = vec2(pos+offset);
        triangles3[i] = target;
        pos += offset;
    }
    pos = vec2(1.1, 0.0);
    offset = vec2(0.0, 1.0/5.0);
    for (int i = 12; i < 18; i++) {
        vec2 target = vec2(0.5, 0.5);
        target += normalize(pos-target)*(0.4);
        triangles1[i] = vec2(pos-offset);
        triangles2[i] = vec2(pos+offset);
        triangles3[i] = target;
        pos += offset;
    }
    pos = vec2(0.0, -0.1);
    offset = vec2(1.0/5.0, 0.0);
    for (int i = 18; i < 24; i++) {
        vec2 target = vec2(0.5, 0.5);
        target += normalize(pos-target)*(0.4);
        triangles1[i] = vec2(pos-offset);
        triangles2[i] = vec2(pos+offset);
        triangles3[i] = target;
        pos += offset;
    }
    float blend = 0.0;
    bool outline = false;
    float triangleRot = 73;
    for (int i = 0; i < 8; i++) {
        bool maybeOutline = true;
        float rot = time*10.0;
        for (int j = 0; j < triangles1.length(); j++) {
            vec2 coord = texCoord + offsets[i];
            coord.x = clamp(coord.x, 0, 1);
            coord.y = clamp(coord.y, 0, 1);
            vec2 triangle1 = triangles1[j];
            vec2 triangle2 = triangles2[j];
            vec2 triangle3 = triangles3[j];
            if (pointInTriangle(coord, triangle1, triangle2, triangle3+(vec2(sin(radians(rot)), cos(radians(rot)))*0.025))) {
                maybeOutline = false;
            }
            rot += triangleRot;
        }
        if (maybeOutline) {
            outline = true;
        }
    }
    float rot2 = time*10.0;
    for (int j = 0; j < triangles1.length(); j++) {
        vec2 triangle1 = triangles1[j];
        vec2 triangle2 = triangles2[j];
        vec2 triangle3 = triangles3[j];
        float alpha = pointInTriangle(texCoord, triangle1, triangle2, triangle3+(vec2(sin(radians(rot2)), cos(radians(rot2)))*0.025)) ? 1.0 : 0.0;
        if (blend < alpha) {
            blend = alpha;
        }
        rot2 += triangleRot;
    }
    if (outline) {
        return vec4(outlineColor, blend);
    }
    return vec4(color, blend);
}

void main() {
    vec3 outlineColor = vec3(1.0, 0.0, 1.0);
    vec3 insideColor = vec3(0.0, 0.0, 0.0);
    float blend = 0.0;

    vec4 color = getColorForPos(insideColor, outlineColor);
    blend = color.a;

    fragColor = vec4(mix(texture(DiffuseSampler, texCoord).rgb, vec3(color.rgb), blend), 1.0);
}