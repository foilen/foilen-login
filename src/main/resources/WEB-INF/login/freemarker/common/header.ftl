<#import "/spring.ftl" as spring />
<#import "spring.ftl" as spring2 />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />

<title>FoilenLogin</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0" />

<!-- Favicon -->
<link rel="icon" type="image/png" href="/images/favicon.png" />
<link rel="shortcut icon" type="image/png" href="/images/favicon.png" />

<link href="<@spring.url'/bundles/all.css'/>" rel="stylesheet">
<script src="<@spring.url'/bundles/all.js'/>"></script>
 
</head>
<body>

<!-- Navbar -->
<div class="header">
  <div class="container-fluid">

    <div class="navbar navbar-default" role="navigation">
      <div class="navbar-header">
        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
          <span class="sr-only">Toggle navigation</span> <span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span>
        </button>
        <a class="navbar-brand" href="/">
          <div id="logo-large" class="hidden-xs"></div>
          <div id="logo-xsmall" class="visible-xs"></div>
        </a>
        <a class="navbar-brand" href="/">Login</a>
      </div>
      <div class="navbar-collapse collapse">

        <!-- Menu -->
        <ul class="nav navbar-nav">
          <#if !user??><li><a href="/login"><@spring2.message 'navbar.login'/></a></li></#if>
          <#if user??><li><a href="/account/"><@spring2.message 'navbar.account'/></a></li></#if>
          <#if isAdmin><li><a href="/admin/"><@spring2.message 'navbar.admin'/></a></li></#if>
        </ul>
        <!-- /Menu -->


        <ul class="nav navbar-nav navbar-right right-navigation">

          <!-- Logout -->
          <#if user??><li><a href="/logout" class="btn btn-danger"><@spring2.message 'navbar.logout'/></a></li></#if>
          <!-- /Logout -->

          <!-- Lang -->
          <li><a href="?lang=<@spring2.message 'navbar.nextlang.id'/>"><@spring2.message 'navbar.nextlang.name'/></a></li>
          <!-- /Lang -->
        </ul>


      </div>
    </div>
  </div>
</div>
<!-- /Navbar -->

<!-- Content -->
<div class="container">
  <div class="row">
    