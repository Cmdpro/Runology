#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D DiffuseDepthSampler;

in vec2 texCoord;
out vec4 fragColor;

float near = 0.1;
float far  = 1000.0;
float LinearizeDepth(float depth)
{
    float z = depth * 2.0 - 1.0;
    return (near * far) / (far + near - z * (far - near));
}

const float exposure = 2.;
const float AOE = 15.;
void main(){
    float depth = LinearizeDepth(texture2D(DiffuseDepthSampler, texCoord).r);
    vec4 color = texture2D(DiffuseSampler,texCoord);
    fragColor = vec4(color.rgb/depth,color.a);
}
