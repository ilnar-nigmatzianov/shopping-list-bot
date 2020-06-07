<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <link rel="stylesheet" href="/static/css/style.css">
    <title>Title</title>
</head>
<body>
<div id="app" class="container">
    <div id="list" data-id="${shoppingList.id}">
        <#list shoppingList.items as item>
            <#if item.isPending()>
                <div>
            <#else>
                <div class="checked">
            </#if>
                    <b>${item.id}</b>
                    <span>${item.text}</span>
                    <button class="checklist-button" data-id="${item.id}">Check</button>
                </div>
        </#list>
    </div>
    </div>
</div>
<#if profile == "dev">
    <script src="http://localhost:8000/main.js"></script>
<#else>
    <script src="/js/main.js"></script>
</#if>
</body>
</html>