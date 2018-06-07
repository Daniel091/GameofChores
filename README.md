# PemGAMI


# Database Example JSON
```
{
  "users" : {
    "FTSyyQqR2kYGQUscp0kCiH10QC93" : {
      "email" : "d-eichinger@arcor.de",
      "name" : "NeuerUsr",
      "uid" : "FTSyyQqR2kYGQUscp0kCiH10QC93",
      "wg_id" : "-LDvZLu-WnyoR9g1ICLf"
    },
  
    "sR2r9jFEurhZ4MM4iRFGOBxoXkz2" : {
      "email" : "deichinger0@gmail.com",
      "name" : "deichinger0@gmail.com",
      "uid" : "sR2r9jFEurhZ4MM4iRFGOBxoXkz2",
      "wg_id" : "-LDvZLu-WnyoR9g1ICLf"
    },
  },
  "wg_tasks" : {
    "-LDvZLu-WnyoR9g1ICLf" : [ {
      "duration" : 10,
      "name" : "Bad putzen",
      "rotatable" : true,
      "time" : 1528322400914,
      "user" : "sR2r9jFEurhZ4MM4iRFGOBxoXkz2"
    }, {
      "duration" : 10,
      "name" : "Bad putzen",
      "rotatable" : true,
      "time" : 1528927200914,
      "user" : "FTSyyQqR2kYGQUscp0kCiH10QC93"
    },]
  },
  "wgs" : {
    "-LDvZLu-WnyoR9g1ICLf" : {
      "admin" : "sR2r9jFEurhZ4MM4iRFGOBxoXkz2",
      "name" : "FancyWG",
      "uid" : "-LDvZLu-WnyoR9g1ICLf",
      "users" : [ "sR2r9jFEurhZ4MM4iRFGOBxoXkz2", "FTSyyQqR2kYGQUscp0kCiH10QC93" ]
    },
  }
}
```





## Git Tutorial

### Branching:
New Branch, switch to branch, commit push
```
git branch name_of_branch 
git checkout name_of_branch
git push origin name_of_branch
```

### Merging:
```
git checkout master
git merge name_of_branch_to_merge
```
-> write commit message for this merge then push it; (in vim ESC; :exit)

### Delete branch

locally: `git branch -d name_of_branch`
remote: `git push origin --delete the_remote_branch`


