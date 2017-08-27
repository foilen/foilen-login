<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8"/>
<title>Mot de passe temporaire</title>
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
<h1>Mot de passe temporaire</h1>
<p>Vous pouvez vous connecter en utilisant le mot de passe suivant: ${password}</p>
<p>Si vous ne l'utilisez pas dans les prochaines 15 minutes, il va expirer et vous devrez en demander un nouveau.</p>
<p>Vous pouvez aussi <a href="${url}">cliquer ici pour l'utiliser automatiquement</a>.</p>
</body>
</html>
