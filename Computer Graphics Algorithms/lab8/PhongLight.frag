varying vec3 n;
varying vec3 v;

void main(void)
{
    vec4 result = vec4(0.0);
    for (int li = 0; li < gl_MaxLights; ++li)
    {
        vec3 lightDirection;
        if (gl_LightSource[li].position.w != 0.0)
        {
            // позиционный источник света
            lightDirection = normalize(gl_LightSource[li].position.xyz - v);
        }
        else
        {
            // направленный источник света
            lightDirection = normalize(gl_LightSource[li].position.xyz);
        }
        vec3 viewDirection = normalize(-v);
        vec3 reflectDirection = normalize(-reflect(lightDirection, n));

        vec4 Iamb = gl_FrontLightProduct[li].ambient;

        float diffuseAngle = max(dot(n, lightDirection), 0.0);
        vec4 Idiff = gl_FrontLightProduct[li].diffuse * diffuseAngle;
        Idiff = clamp(Idiff, 0.0, 1.0);

        float specularAngle = max(dot(reflectDirection, viewDirection), 0.0);
        vec4 Ispec = gl_FrontLightProduct[li].specular * pow(specularAngle, gl_FrontMaterial.shininess * 4.0);
        Ispec = clamp(Ispec, 0.0, 1.0);

        result += 2.0* Iamb + 2.0* Idiff + Ispec;
    }

    gl_FragColor = gl_FrontLightModelProduct.sceneColor + result;
}