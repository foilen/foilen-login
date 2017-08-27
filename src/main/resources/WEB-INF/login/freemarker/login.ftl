<#include "common/header.ftl">

<div>
  <!--<div class="col-md-3">
    <p><@spring2.message 'login.methods'/></p>
    
    <ul class="nav nav-pills nav-stacked">
      <li class="active"><a href="#passwordTab" aria-controls="passwordTab" data-toggle="pill"><@spring2.message 'login.methods.password'/></a></li>
      <li><a href="#other" aria-controls="other" data-toggle="pill">Other</a></li>
    </ul>
    
  </div>
  <div class="col-md-9"> -->
  <div class="col-md-12">
  
    <div class="tab-content">
      <div class="tab-pane active" id="passwordTab">
        <h1><@spring2.message 'login.password.header'/></h1>

        <#if error??>
          <p class="bg-danger"><@spring2.message error/></p>
        </#if>
        <#if success??>
          <p class="bg-success"><@spring2.message success/></p>
        </#if>
    
        <form id="passwordForm" method="post" action="/handler/password">
          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
          <input id="token" name="token" type="hidden" value="${token}" />
          <div class="form-group">
            <label for="email"><@spring2.message 'login.password.form.email'/></label>
            <input id="email" name="email" type="email" class="form-control" placeholder="<@spring2.message 'login.password.form.email.placeholder'/>" value="${(form.email)!}" />
          </div>
          <div class="form-group">
            <label for="password"><@spring2.message 'login.password.form.password'/></label>
            <input id="password" name="password" type="password" class="form-control" placeholder="<@spring2.message 'login.password.form.password.placeholder'/>" />
          </div>
          <button id="passwordSubmit" type="submit" class="btn btn-primary"><@spring2.message 'login.password.form.login'/></button>
        </form>
        
        <hr/>
        <a id="oneTime" class="btn btn-default"><@spring2.message 'login.password.form.onetime'/></a>
        <p><@spring2.message 'login.password.form.onetime.details'/></p>
        
      </div>
      
      <!--<div class="tab-pane" id="other">...by other...</div>-->
      
    </div>
    
  </div>
  
</div>

<script type="text/javascript">
  jQuery(document).ready(function(){
    jQuery('#oneTime').click(function(){

      // Update the form action
      var form = jQuery('#passwordForm');
      form.attr('action', '/handler/createOneTimePassword');
      
      // Check an email is present (or update the form)
      if (jQuery('#email').val() == '') {
        jQuery('#password').parent().hide();
        jQuery('#passwordSubmit').hide();
        return
      } 

      // Submit
      form.submit();
      
    });
  });
</script>

<#include "common/footer.ftl">
