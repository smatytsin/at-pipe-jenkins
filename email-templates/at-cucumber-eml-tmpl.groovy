<STYLE>
  BODY, TABLE, TD, TH, P {
    font-family: Calibri, Verdana, Helvetica, sans serif;
    font-size: 12px;
    color: black;
  }
  .console {
    font-family: Courier New;
  }
  .filesChanged {
    width: 10%;
    padding-left: 10px;
  }
  .section {
    width: 100%;
    border: thin black dotted;
  }
  .td-title-main {
    color: white;
    font-size: 200%;
    padding-left: 5px;
    font-weight: bold;
  }
  .td-title {
    color: white;
    font-size: 120%;
    font-weight: bold;
    padding-left: 5px;
    text-transform: uppercase;
  }
  .td-title-tests {
    font-weight: bold;
    font-size: 120%;
  }
  .td-header-maven-module {
    font-weight: bold;
    font-size: 120%;    
  }
  .td-maven-artifact {
    padding-left: 5px;
  }
  .tr-title {
    background-color: <%= (build.result == null || build.result.toString() == 'SUCCESS') ? '#27AE60' : build.result.toString() == 'FAILURE' ? '#E74C3C' : '#f4e242' %>;
  }
  .test {
    padding-left: 20px;
  }
  .test-fixed {
    color: #27AE60;
  }
  .test-failed {
    color: #E74C3C;
  }
</STYLE>
<BODY>
  <!-- BUILD RESULT -->
  <table class="section">
    <tr class="tr-title">
      <td class="td-title-main" colspan=2>
        ��������: ${build.result ?: 'COMPLETED'}
      </td>
    </tr>
    <tr>
      <td>������:</td>
      <td><A href="${rooturl}${build.url}">${rooturl}${build.url}</A></td>
    </tr>
    <tr>
      <td>������:</td>
      <td>${project.name}</td>
    </tr>
    <tr>
      <td>����:</td>
      <td>${it.timestampString}</td>
    </tr>
    <tr>
      <td>�����������������:</td>
      <td>${build.durationString}</td>
    </tr>
    <tr>
      <td>Allure:</td>
      <td><A href="${rooturl}${build.url}allure/">${rooturl}${build.url}allure/</A></td>
    </tr>
    <tr>
      <td>����� �������:</td>
      <td><A href="${rooturl}${build.url}console">${rooturl}${build.url}console</A></td>
    </tr>
  </table>
  <br/>


<!-- JUnit TEMPLATE -->

  <%
  def junitResultList = it.JUnitTestResult
  try {
    def cucumberTestResultAction = it.getAction("org.jenkinsci.plugins.cucumber.jsontestsupport.CucumberTestResultAction")
    junitResultList.add( cucumberTestResultAction.getResult() )
  } catch(e) {
    //cucumberTestResultAction not exist in this build
  }
  if ( junitResultList.size() > 0 ) { %>
  <table class="section">
    <tr class="tr-title">
      <td class="td-title" colspan="5">${junitResultList.first().displayName}</td>
    </tr>
    <tr>
        <td class="td-title-tests">Failed</td>
        <td class="td-title-tests">Passed</td>
        <td class="td-title-tests">Skipped</td>
        <td class="td-title-tests">�����</td>
      </tr>
    <% junitResultList.each {
      junitResult -> junitResult.getChildren().each {
        packageResult -> %>
    <tr>
      <td>${packageResult.getFailCount()}</td>
      <td>${packageResult.getPassCount()}</td>
      <td>${packageResult.getSkipCount()}</td>
      <td>${packageResult.getPassCount() + packageResult.getFailCount() + packageResult.getSkipCount()}</td>
    </tr>
    <% packageResult.getPassedTests().findAll({it.getStatus().toString() == "FIXED";}).each{
        test -> %>
            <tr>
              <td class="test test-fixed" colspan="5">
                ${test.getFullName()} ${test.getStatus()}
              </td>
            </tr>
        <% } %>
        <% packageResult.getFailedTests().sort({a,b -> a.getAge() <=> b.getAge()}).each{
          failed_test -> %>
    <tr>
      <td class="test test-failed" colspan="5">
        ${failed_test.getFullName()} (Age: ${failed_test.getAge()})
      </td>
    </tr>
        <% }
      }
    } %>
  </table>
  <br/>
  <% } %>

<!-- CONSOLE OUTPUT -->
  <%
  if ( build.result == hudson.model.Result.FAILURE ) { %>
  <table class="section" cellpadding="0" cellspacing="0">
    <tr class="tr-title">
      <td class="td-title">CONSOLE OUTPUT</td>
    </tr>
    <% 	build.getLog(100).each() {
      line -> %>
	  <tr>
      <td class="console">${org.apache.commons.lang.StringEscapeUtils.escapeHtml(line)}</td>
    </tr>
    <% } %>
  </table>
  <br/>
  <% } %>
</BODY>