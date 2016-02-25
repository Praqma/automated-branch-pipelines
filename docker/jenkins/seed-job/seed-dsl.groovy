/**
 * A very basic build pipeline with commit, test and release jobs.
 *
 * Assumes these Jenkins job build parameters:
 * BRANCH - the branch name, used in job names
 */

import javaposse.jobdsl.dsl.DslException

// Read expected build parameters
def parameter = 'BRANCH'
def branch = binding.variables.get(parameter)
if (!branch) {
  throw new DslException('Build parameter ' + parameter + ' is missing')
}

def commitJob = 'commit_' + branch

job(commitJob) {
  description 'Build source code and run unit tests'

  scm {
    github('Praqma/automated-branch-pipelines')
  }

  steps {
    shell('echo Generated by the commit job > commit-artifact.txt')
  }

  publishers {
    archiveArtifacts {
      pattern('commit-artifact.txt')
    }
  }
}

job('test_' + branch) {
  description 'Acceptance tests'

  steps {
    copyArtifacts(commitJob) {
      includePatterns('commit-artifact.txt')
    }
    shell('cat commit-artifact.txt')
  }
}

job('release_' + branch) {
  description 'Package a release'

  steps {
    copyArtifacts(commitJob) {
      includePatterns('commit-artifact.txt')
    }
    shell('cp commit-artifact.txt release-artifact.txt')
  }

  publishers {
    archiveArtifacts {
      pattern('release-artifact.txt')
    }
  }
}
