package fish.payara.maven.plugins.dynamicproperties;
import java.util.Properties;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

/**
 * A mojo used to split the version of the project into a set
 * of accesible project properties based on a given delimiter
 * 
 * If there are more splits in the result than parameter names
 * given, the final parameter will be filled with all overloaded
 */
@Mojo(name = "split-version-by-delimiter", defaultPhase = LifecyclePhase.VALIDATE)
public class VersionSeparation extends AbstractMojo {

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    MavenProject project;

    @Parameter(defaultValue = "\\.", required = true)
    String delimiter;

    @Parameter(required = true)
    String[] parameterNames;

    String versionString;

    public void execute() throws MojoExecutionException, MojoFailureException {
        // Perform a split on the value by the delimiter
        versionString = project.getVersion();
        String[] versionComponents = versionString.split(delimiter);

        Properties properties = project.getProperties();

        getLog().info("Creating Properties with New Array: ");
        for(String entry : parameterNames) {
            getLog().info("- " + entry);
        }

        // Ensure that our parameterNames is the same length or shorter than the elements we've got
        if(parameterNames.length > versionComponents.length) {
            getLog().error("There aren't enough values from the split on " + versionString + " to fill the parameters, please review your settings");
            return;
        }
        if(parameterNames.length < versionComponents.length) {
            getLog().warn("More data from version split than properties given - will overload final property with excess data");
        }

        int indexCache = 0;
        for(int i = 0; i < parameterNames.length - 1; i++) {
            properties.setProperty(parameterNames[i], versionComponents[i]);
        }

        String finalComponent = "";
        for(int i = indexCache; i < versionComponents.length - 1; i++) {
            finalComponent += versionComponents[i] + delimiter;
        }
        
        finalComponent += versionComponents[versionComponents.length - 1];
        properties.setProperty(parameterNames[parameterNames.length - 1], finalComponent);

    }

}