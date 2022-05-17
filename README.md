# Dynamic Build Properties Plugin

This Maven plugin gives the ability to dynamically generate build properties based on values for other properties defined in a given pom.

The use-case justifying its creation comes from the desire to automate versioning in Payara Server where values for branding properties are given from major_ minor_ and update_ version properties in the top-level pom. These should be generated automatically via a split on the project.version property of the project.