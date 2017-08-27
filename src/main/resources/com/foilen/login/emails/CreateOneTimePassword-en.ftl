<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8"/>
<title>Single Use Password</title>
<style>

.right {
  float: right;
}

.clear {
  clear: both;
}

h1 {
  color: #0011CE;
  border-bottom: solid 3px #001178;
  font-size: 16px;
}

.alignRight {
  text-align: right;
}

hr {
  border-bottom: solid 1px #001178;
}

</style>
</head>
<body>

<img class="right" src="cid:logo"/>
<div class="clear"></div>
<h1>One Time Password</h1>
<p>You can log on using the following password: ${password}</p>
<p>If you do not use it in the next 15 minutes, it will expire and you will need to request a new one.</p>
<p>You can also <a href="${url}">click here to automatically use it</a>.</p>
</body>
</html>
