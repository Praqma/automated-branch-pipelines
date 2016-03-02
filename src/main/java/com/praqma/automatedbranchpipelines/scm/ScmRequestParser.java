package com.praqma.automatedbranchpipelines.scm;

import java.io.InputStream;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

/**
 * Parses HTTP requests from SCM systems.
 * <p>
 *
 * The request body should be JSON. It should always contain these entries:
 * <ul>
 *   <li>scm : The SCM type, for example "git"</li>
 *   <li>repository: The repository name, for example "test-abs"</li>
 *   <li>branch : The branch prefix and name, for example "feature/1337-coolfeature"</li>
 * </ul>
 * <p>
 *
 * Details of a Git/Stash/Bitbucket Server request:
 * <ul>
 *  <li>scm is "git"</li>
 *  <li>action can be "ADD", "UPDATE" or "DELETE". ADD signals branch creation, DELETE
 *      signals branch deletion</li>
 * </ul>
 */
class ScmRequestParser {

  /** The SCM identifier for Git. */
  private static final String SCM_GIT = "git";

  /** Bitbucket/git refChange type indicating branch creation. */
  private static final String GIT_CREATE_ACTION = "ADD";

  /** Bitbucket/git refChange type indicating branch deletion. */
  private static final String GIT_DELETE_ACTION = "DELETE";

  /**
   * Reads a JSON request body.
   *
   * @throws ScmRequestException if the request body is invalid
   */
  static ScmRequest parse(InputStream requestBody) throws ScmRequestException {
	  try (JsonReader reader = Json.createReader(requestBody)) {
      JsonObject json;
      try {
        json = reader.readObject();
      } catch (Exception e) {
        throw new ScmRequestException("Invalid JSON request body", e);
      }

      String scm = readKey("scm", json);

      if (SCM_GIT.equals(scm)) {
        return parseGit(json);
      } else {
        throw new ScmRequestException("Unknown SCM type: " + scm);
      }
    }
  }

  private static ScmRequest parseGit(JsonObject json) throws ScmRequestException {
    String repository = readKey("repository", json);
    Branch branch = parseBranch(json);
    Action action = parseGitAction(json);

    return new ScmRequest(repository, branch, action);
  }

  private static Branch parseBranch(JsonObject json) throws ScmRequestException {
    String urlEncoded = readKey("branch", json);
    return new Branch(urlEncoded);
  }

  private static Action parseGitAction(JsonObject json) throws ScmRequestException {
    String action = readKey("action", json);
    if (GIT_CREATE_ACTION.equals(action)) {
      return Action.CREATE;
    } else if (GIT_DELETE_ACTION.equals(action)) {
      return Action.DELETE;
    } else {
      return Action.IGNORE;
    }
  }

  private static String readKey(String key, JsonObject json) throws ScmRequestException {
    if (!json.containsKey(key)) {
      throw new ScmRequestException("JSON request body does not contain key '" + key + "''");
    }
    return json.getString(key);
  }

}
