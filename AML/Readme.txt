Instructions:
1. For the Structure Learning assignment there are 2 packages aml and MpaAlgos.
2. aml contains ParamEst.java, the code for learning the feature parameters. The other package has Message Passing Algorithm implementation.
3. The file format I have used is given in the file: paramest.txt
4. To compile: javac aml/*.java and javac MpaAlgos/*.java
5. To run: java aml.ParamEst <file containing the data>
6. Since the MPA algorithm has not been completely implemented by us we are using a previous implementation by Rahul Sharnagat as a BlackBox. Kindly do not evaluate the Message Passing algorithm assignament on the basis of the code in MpaAlgos. It will be submitted separately.
7. The assumption is that the training data has all information. For all possible clique value combinations there must me atleast 1 feature that fires for each possible value.