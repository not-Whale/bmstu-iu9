
varying vec3 n;
varying vec3 v;

void main()
{   
    v = vec3(gl_ModelViewMatrix * gl_Vertex);
    n = normalize(gl_NormalMatrix * gl_Normal);
    gl_Position = gl_ModelViewProjectionMatrix * vec4(gl_Vertex.x, gl_Vertex.y, gl_Vertex.z, 1);
}
