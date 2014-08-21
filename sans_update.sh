# Loginto ssh -X ilmt@10.144.22.105 (pass is ilmt123)

# run mysql -u dipak -ptmp123 < sqlcommand > a.sql
# Come to this folder and do : scp -r ilmt@10.144.22.105:/users/proj/ilmt/a.sql . 

#Generate temporary files to work on


cp sanskrit-merged.syns sanstest

#Convert into a convinient format

sed -i 's/\t//g' sanstest
sed -i 's/:: /::/g' sanstest 

#Run like the blazes

python update_swn_db.py sanstest


