Dynatrace Build Notifier Plugin for Jenkins
=======================================

This Jenkins plugin notifies Dynatrace of build results. Failed or
successful builds will show up as little icons in the Dynatrace web 
interface in commit logs. Clicking on such an icon will take the 
user to the specific build.

Requirements
============

* **[Dynatrace][] 2.1** or newer. This plugin uses the  
[Dynatrace Build REST API][] which was introduced with Dynatrace 2.1. 
* **Jenkins 1.498** or newer

Setup
=====

Set up Dynatrace Notifier by navigating to `Manage Jenkins --> Configure System` and scrolling down to the **Dynatrace Notifier** section. Enter _at least_ your `Dynatrace Root Url` and `Credentials`. Additional options are available as required. (Screenshot below is from Jenkins v2.44)

![Dynatrace Notifier Settings](images/DynatraceNotifierSettings.png)

Usage
=====

Use the Dynatrace Notifier by adding it as a _Post Step_ in your Jenkins build job 
configuration. 

1. In your Jenkins job configuration go to the *Post-build Actions* section, 
click on *Add post-build action* and select *Notify Dynatrace Instance*
2. Enter the Dynatrace base URL, e. g. <tt>http://localhost:7990</tt> or 
<tt>http://my.company/dynatrace</tt>. If in doubt, go to your local Dynatrace 
server and check the URL in the browser. The URL 
<tt>http://georg@localhost:7991/projects</tt> e. g. reveals the
server base URL, which is <tt>http://localhost:7991</tt> in this case. 
2. Use the [Credentials Plugin](https://wiki.jenkins-ci.org/display/JENKINS/Credentials+Plugin) to select credentials for dynatrace

That's it. If you have configured everything correctly, Jenkins will notify
your Dynatrace instance of subsequent builds. The result is illustrated on
s [Dynatrace Build Integration][] wiki page.

### Note on Pipeline Plugin usage

See the following code for an example of how to use this plugin inside of a 
[Pipeline Plugin](https://wiki.jenkins-ci.org/display/JENKINS/Pipeline+Plugin). You must set the result of the 
current build manually in the Pipeline script.

```groovy
node {
    checkout scm                            // Necessary so we know the current commit

    notifyDynatrace()                       // Notifies the Dynatrace Instance of an INPROGRESS build

    try {
        // Do stuff

        currentBuild.result = 'SUCCESS'     // Set result of currentBuild !Important!
    } catch(err) {
        currentBuild.result = 'FAILED'      // Set result of currentBuild !Important!
    }

    notifyDynatrace()                       // Notifies the Dynatrace Instance of the build result
}
```

In situations where an advanced setup is required the following can be used:
```groovy
node {
    this.notifyDynatrace('INPROGRESS')     // Notifies the Dynatrace Instance of an INPROGRESS build
    
    try {
        // Do stuff
    
        this.notifyDynatrace('SUCCESS')
    } catch(err) {
        this.notifyDynatrace('FAILED')
    }
}

def notifyDynatrace(String state) {

    if('SUCCESS' == state || 'FAILED' == state) {
        currentBuild.result = state         // Set result of currentBuild !Important!
    }
    notifyDynatrace commitSha1: "commit", 
                credentialsId: '00000000-1111-2222-3333-123456789abc', 
                disableInprogressNotification: false, 
                considerUnstableAsSuccess: true, 
                ignoreUnverifiedSSLPeer: true, 
                includeBuildNumberInKey: false, 
                prependParentProjectKey: false, 
                projectKey: '', 
                dynatraceServerBaseUrl: 'https://dynatrace.company.com'

}
```

In [Declarative Pipelines](https://jenkins.io/doc/book/pipeline/syntax/#declarative-pipeline), where Jenkins sets `currentBuild.result = null` for `SUCCESS` builds, the current value can be modified via a `script` step, e.g.:

```groovy
pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                echo 'Hello World'
                // currentBuild.result == null here
            }
        }
    }
    post { 
        always { 
            script {
                currentBuild.result = currentBuild.result ?: 'SUCCESS'
                notifyDynatrace()
            }
        }
    }
}
```

### Note on credentials

Currently Dynatrace Build Notifier Plugin accepts only raw plaintext credentials as it works over the HTTP REST API of dynatrace.


Maintainers
===========

* Georg Gruetter ([Twitter](https://twitter.com/bumbleGee), [GitHub](https://github.com/gruetter))
* Pavel Batanov ([GitHub](https://github.com/scaytrase))

License
=======

[Apache 2.0 License](http://www.apache.org/licenses/LICENSE-2.0.html)

[Dynatrace]: www..com/software/dynatrace
[Dynatrace Build Integration]: https://developer..com/dynatrace/docs/latest/how-tos/updating-build-status-for-commits.html
[Dynatrace Build REST API]: https://developer..com/static/rest/dynatrace/latest/dynatrace-build-integration-rest.html

