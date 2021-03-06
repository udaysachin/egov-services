def notifyBuild(String buildStatus = 'STARTED') {
  buildStatus =  buildStatus ?: 'SUCCESSFUL'
  def BUILD_STATUS = "${buildStatus}"
  def colorName = 'RED'
  def colorCode = '#FF0000'
  def subject = "${buildStatus}: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'"
  def summary = "${subject} (${env.BUILD_URL})"

  if (buildStatus == 'STARTED') {
    color = 'YELLOW'
    colorCode = '#FFFF00'
  } else if (buildStatus == 'SUCCESSFUL') {
    color = 'GREEN'
    colorCode = '#00FF00'
  } else {
    color = 'RED'
    colorCode = '#FF0000'
  }

    slackSend (color: colorCode, message: summary)
    emailext (
      body: '${SCRIPT, template="groovy-html.template"}', recipientProviders: [[$class: 'CulpritsRecipientProvider'], [$class: 'DevelopersRecipientProvider'], [$class: 'RequesterRecipientProvider']], replyTo: '$DEFAULT_REPLYTO', subject: '$PROJECT_NAME - Build # $BUILD_NUMBER - ${BUILD_STATUS}!', to: '$DEFAULT_RECIPIENTS'
    )
}

return this;
