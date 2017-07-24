#version 330 core

in vec4 spriteColor;
in vec2 TexCoords;
out vec4 color;

uniform sampler2D ourTex0;


void main()
{
    
  	color =  spriteColor * texture(ourTex0, TexCoords);
  	
  	if (color.w < 1 )
  		discard;
}
