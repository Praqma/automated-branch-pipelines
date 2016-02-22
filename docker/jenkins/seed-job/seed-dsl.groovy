job('commit') {
  description 'Build source code and run unit tests'

  scm {
    github('Praqma/automated-branch-pipelines')
  }

  steps {
    shell('cat README.md')
  }

}
