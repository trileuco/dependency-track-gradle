# Dependency-track gradle plugin

This is a community plugin that helps you with bom publication to your Dependency-Track server.

> :warning: **This plugin does not generate the bom itself**

### Using the plugin

We choose [cycloneDx plugin](https://github.com/CycloneDX/cyclonedx-gradle-plugin) to generate the bom.

In the target project, add the plugins as usual:
```
plugins {
    id "org.cyclonedx.bom" version "1.4.1"
    id 'dependency-track' version "0.1.0"
}
```

Configure your custom properties:

```
dependencyTrack {
    host = 'your-dtrack-server-host'
    apiKey = 'your-dtrack-api-key'
    projectId = 'your-dtrack-project-identifier'
}
tasks.named('dependencyTrack') {
    dependsOn cyclonedxBom
}
```

Run with:
```
./gradlew dependencyTrack -i
```

#### Default properties:

The full list of supported properties with their default values:

```
dependencyTrack {
    host = 'http://localhost:8081'
    realm = '/api/v1/bom'
    bomFile= file("${buildDir.path}/reports/bom.xml")
    projectId = ''
    apiKey = ''
}
tasks.named('dependencyTrack') {
    dependsOn cyclonedxBom
}
```
