# PemGAMI

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


