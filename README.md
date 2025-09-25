The Media Archive Management System is a program designed to manage media 
data provided by users. It allows users to conduct search tests, retrieve specific media 
information using a key, list the top 10 media items based on user votes, display all 
media streams available in a given country, and identify media items that are 
streaming on all five platforms.

System uses 2 different hash functions:
1.Simple Summation Function (SSF)
<img width="123" height="66" alt="image" src="https://github.com/user-attachments/assets/f459ce28-4865-4c81-8caa-a2bcb5cb843f" />

2.Polynomial Accumulation Function (PAF)
<img width="438" height="28" alt="image" src="https://github.com/user-attachments/assets/a62e1fa1-74f9-4356-9f6a-d50a02f46a42" />

System uses 2 different Collision Handling Functions:

1.Linear Probing (LP)
 Linear probing handles collisions by placing the colliding item in the next (circularly) available table cell.

2.Double Hashing (DH)
 Double hashing uses a secondary hash function d(k) and handles collisions by placing an item in the first available cell of the series.
 <img width="249" height="81" alt="image" src="https://github.com/user-attachments/assets/83bf0969-03d1-489a-a31e-c088e94506b5" />
 where q < N (table size), q is a prime, and j = 0, 1, … , N – 1.

 The system was used to monitor performance across a set of inputs provided in a .csv file, which included different load factors, hash functions, and collision handling methods.
 Here is the results:
 <img width="543" height="234" alt="image" src="https://github.com/user-attachments/assets/5df2255d-6b25-4884-8eeb-fd15a62f72ad" />
