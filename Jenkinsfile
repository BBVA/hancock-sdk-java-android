// Please read the documentation about Shuttle's Jenkins Pipelines for further information
// https://ndbkickstart.atlassian.net/wiki/spaces/XTEAM/pages/153550863/Jenkinsfile+Reference

javaPipeline{

      //Generate Unit Tests Files
      stage("Unit Tests"){
        //TOPO Generate reports in the folder reports directly
        container("jdk"){
          sh """
            ./gradlew test --stacktrace --debug
            ./gradlew jacocoTestReport jacocoTestCoverageVerification --stacktrace
            mkdir tests/reports/unit
            mkdir tests/reports/coverage
            cp -r build/test-results/test/ tests/reports/unit/ 
            cp -r tests/reports/test/  tests/reports/coverage/
          """
        }
      }

      stage("Docs"){
          container("jdk"){
            sh """
                ./gradlew javadoc
            """
            upload_doc_shuttle_stage(docName: "docs", docPath: "./build/docs/javadoc")
          }
      }

}
