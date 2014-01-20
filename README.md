Gestionnaire de comptes Bancaires
==================
    
Projet de création d'un gestionnaire de comptes bancaires basique pour s'exercer sur le J2EE, JSF, Les Webservices et Android

[Sujet du rendu](
http://miageprojet2.unice.fr/Intranet_de_Michel_Buffa/Web_services_REST_%2f%2f_HTML5_MBDS_2013-2014/A_rendre_pour_les_cours_JavaEE%2f%2fWeb_2.0_MBDS_2013-2014)

Date de rendu : **20/01/2014**

Serveur
===

Webservices
===

Pour un serveur sur *http://localhost:8080/GestionnaireDeComptesBanquaires-war/*

Liste de tous les clients
------

URL : **/api/client**

Type : **GET**

Parametres : aucun

Retour : Une liste d'objets Client en JSON
```JSON
[{
    "id": 1,
    "nom": "* Anonymous United! *",
    "login": "anonymous",
    "password": "b5eae6c475333f72ba9ff6884b872b9d",
    "beneficiaires": {},
    "comptes": [{
        "id": 2,
        "solde": 77777.0,
        "nom": "Anonymous's looting",
        "operations": []
    }, {
        "id": 3,
        "solde": 1337000.0,
        "nom": "Anonymous's savings",
        "operations": []
    }]
}, {
    "id": 4,
    "nom": "Hadopi",
    "login": "hadopi",
    "password": "ceeccd4e8d7c5f7396536d4beeb6e32d",
    "beneficiaires": {},
    "comptes": [{
        "id": 5,
        "solde": -200000.0,
        "nom": "Hadopi - compte courant",
        "operations": []
    }]
},
    ...]
```


Login d'un client
------

URL : **/api/client/login**

Type : **POST**

Parametres : **application/x-www-form-urlencoded**
* **login** - le login de l'utilisateur
* **password** - le mot de passe de l'utilisateur

Retour : l'id du client en cas de succès, 0 si identifiants incorrects/inconnus (en text/plain)
```JSON
4
```


Création d'un client
------

URL : **/api/client**

Type : **POST**

Parametres : **application/x-www-form-urlencoded**
* **nom** - le nom du client
* **login** - le login du client
* **password** - le mot de passe du client

Retour : l'id du client crée en text/plain
```JSON
2002
```


Modification d'un client
------

URL : **/api/client**

Type : **PUT**

Parametres : **application/x-www-form-urlencoded**
* **idClient** - l'id du client a modifier
* **nom** - le nom du client
* **login** - le login du client
* **password** - le mot de passe du client

Retour : Un objet Client en JSON
```JSON
{
    "id": 1,
    "nom": "* Anonymous United! *",
    "login": "anonymous",
    "password": "b5eae6c475333f72ba9ff6884b872b9d",
    "beneficiaires": {},
    "comptes": [{
        "id": 2,
        "solde": 77777.0,
        "nom": "Anonymous's looting",
        "operations": []
    }, {
        "id": 3,
        "solde": 1337000.0,
        "nom": "Anonymous's savings",
        "operations": []
    }]
}
```


Suppression d'un client
------

URL : **/api/client/{id}**

Type : **DELETE**

Parametres : **PathParam**
* **{id}** - l'id du client a supprimer

Retour : rien


Obtenir un client
------

URL : **/api/client/{id}**

Type : **GET**

Parametres : **PathParam**
* **{id}** - l'id du client souhaité

Retour : Un objet Client en JSON
```JSON
{
    "id": 1,
    "nom": "* Anonymous United! *",
    "login": "anonymous",
    "password": "b5eae6c475333f72ba9ff6884b872b9d",
    "beneficiaires": {},
    "comptes": [{
        "id": 2,
        "solde": 77777.0,
        "nom": "Anonymous's looting",
        "operations": []
    }, {
        "id": 3,
        "solde": 1337000.0,
        "nom": "Anonymous's savings",
        "operations": []
    }]
}
```


Liste de tous les clients, avec limite
------

URL : **/api/client/{from}/{limit}**

Type : **GET**

Parametres : **PathParam**
* **{from}** début d'interval (0 par exemple)
* **{limit}** nombre max de clients a afficher

Retour : Une liste d'objets Client en JSON
```JSON
[{
        "id": 1,
        "nom": "* Anonymous United! *",
        "login": "anonymous",
        "password": "b5eae6c475333f72ba9ff6884b872b9d",
        "beneficiaires": {},
        "comptes": [{
            "id": 2,
            "solde": 77777.0,
            "nom": "Anonymous's looting",
            "operations": []
        }, {
            "id": 3,
            "solde": 1337000.0,
            "nom": "Anonymous's savings",
            "operations": []
        }]
    }, {
        "id": 4,
        "nom": "Hadopi",
        "login": "hadopi",
        "password": "ceeccd4e8d7c5f7396536d4beeb6e32d",
        "beneficiaires": {},
        "comptes": [{
            "id": 5,
            "solde": -200000.0,
            "nom": "Hadopi - compte courant",
            "operations": []
        }]
    }, 
    ...]
```


Compter le nombre de clients
------

URL : **/api/client/count**

Type : **GET**

Parametres : aucun

Retour : Le nombre de compte en text/plain
```JSON
1000
```


Ajouter un bénéficiaire à un  client
------

URL : **/api/client/benef**

Type : **POST**

Parametres : **application/x-www-form-urlencoded**
* **idClient** - l'id du client a modifier
* **idBeneficiaire** - l'id du bénéficiaire à ajouter
* **labelBeneficiaire** - le label de ce beneficiaire (ex : "proprio")

Retour : L'objet Client modifié en JSON
```JSON
{
    id: 4
    nom: "Hadopi"
    login: "hadopi"
    password: "ceeccd4e8d7c5f7396536d4beeb6e32d"
    beneficiaires: {
        1: "Pwned by the Anonymous"
    } -
        comptes: [1]
    0: {
        id: 5
        solde: -200000
        nom: "Hadopi - compte courant"
        operations: [0]
    }
}
```


Ajouter un bénéficiaire à un  client
------

URL : **/api/client/benef**

Type : **PUT**

Parametres : **application/x-www-form-urlencoded**
* **idClient** - l'id du client a modifier
* **idBeneficiaire** - l'id du CompteBancaire bénéficiaire à ajouter
* **labelBeneficiaire** - le label de ce beneficiaire (ex : "proprio")

Retour : L'objet Client modifié en JSON
```JSON
{
    id: 4
    nom: "Hadopi"
    login: "hadopi"
    password: "ceeccd4e8d7c5f7396536d4beeb6e32d"
    beneficiaires: {
        2: "Donations to the Anonymous"
    } -
        comptes: [1]
    0: {
        id: 5
        solde: -200000
        nom: "Hadopi - compte courant"
        operations: [0]
    }
}
```


Ajouter un bénéficiaire à un  client
------

URL : **/api/client/benef/{id}/{idCompte}/**

Type : **DELETE**

Parametres : **PathParam**
* **{id}** - l'id du client a modifier
* **{idCompte}** - l'id du CompteBancaire beneficiaire a retirer

Retour : Le label du bénéficiaire supprimé, rien sinon (en text/plain)
```JSON
Donations to the Anonymous
```