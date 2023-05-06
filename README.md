# Machine Learning: Q-Learning Algorithm Simulation

## Description of the logic and design of the simulation
The simulation contains 3 kinds of nodes: start, finish, idle and hole. The 
start node and finish are fixed at (0,0) and (total row - 1, total col - 1). The holes are randomly set 
and idle nodes are just normal nodes. 

Rules: 
- Idle nodes have no reward, they give 0 points.
- Hole nodes would cost the AI -1000 points.
- Finish node would reward the AI with 1000 points.
- Visiting the start node back again would cost the AI -10 points.

Now the job of the AI agent is to avoid hole nodes and reach the finish node by following the shortest 
(or highest rewarding) path to it.

Here the agent does a bit of exploration in the beginning and once it finds the goal node, it gets 
trained enough to follow that same path again and again.

### Watch the AI learn by studying its surrounding
![Animation](https://user-images.githubusercontent.com/70837272/236609027-e7d680e1-0aa3-42be-ac2d-c62f0fd30588.gif)

### User Input
In the beginning, you will get a GUI window pop up which will ask you for the grid size. 
There are 2 options here 4x4 and 6x6. The rest of the simulation does not require any user input 
whatsoever. The AI does the rest of the job.

### Logic and design of the simulation

Epsilon formula is just a way for me to mimic the behaviour where the agent would explore around the map 
and once it finds a finish node, it would remember that path and find the shortest path to get to it. 
This behaviour also shows signs of the agent trying to explore other paths (once the path has been 
found) to check whether they are the closest ones or not. Once the program runs for a while, the 
Q-values get stable and the agent would only end up picking one path. Also, the simulation is in an infinite 
loop and once the agent reaches the finish node, it starts back again at its initial position (0, 0).

Usually the AI finds a shortest or highest reward path in around 10 seconds or maybe less than that 
depending on the size of the grid. Some cases where the hole is extremely close to the finish node might 
cause a bit of delay. If the hole nodes are completely surrounding the finish node then the AI will not 
find the finish node, as AI would think that specific region to be not that rewarding. This is because 
the nodes surrounding the finish node and their neighbouring nodes would have a negative Q-value which 
would not be a likely choice for AI.


### Working of the Q-Learning Algorithm
The Q-Learning algorithm in my case works as follows, here we will consider that we already have a 
currentNode:
- We first create a clone of that vector and get the set of possible actions (move up, down, right or 
left).
- We then go through each one of those and get the respective nodes. Here we use the epsilon value, in 
the `AI for Games` book, epsilon is refered to as rho. And it is a constant value, whereas in my case 
this value changes, it depends on the number of episodes. When the number of episode increases, this 
value decreases. So, this gives a nice balance between exploring and exploiting known path. We compare a 
random value between [0,1] with this epsilon, if the random value is greater then we get neighbouring 
node with highest q-value, else we get a random neighboring node.
- Now that we know the neighbouring node, we get its q-value. We get the max q-value of its neighbouring 
nodes and we get its reward value. Using these values we set/update its q-value using bellman's 
equation. 
- Then we update the currentPosition to that of the next node. And the cycle continues.

In my program, I train my AI 10000 times and even more with the available q-value and reward data. The 
Q-value table I have from the beginning of the execution, does not contain information regarding hole 
nodes and finish node. The AI needs to explore by going from one neighboring node to another, by this we 
are getting access to reward values and previous node's Q-value which helps us calculate the Q-values of 
the next neighbouring nodes. My Q-value algorithm visits several neighboring nodes of the current node 
at a time and picks the neighbour with the highest value. That node is returned and that becomes my next 
current node. Likewise, I visit other nodes and set their Q-values one by one. This behaviour shows 
signs of exploring and learning, which is something I wanted to implement in this project.


### File Structure
- `Action.java`: A enum class which contains information regarding directions the agent can visit.
- `Main.java`: The class which gets compiled and executed. This class will set the JFrame components and 
also would ask the user for gridSize input.
- `Game.java`: This class has the main logic of the simulation and contains the q-learning algorithm..
- `Node.java`: Use for managing node logic in the simulation. Like setting their state to idle, finish, hole or start.
- `Vector.java`: Helpful for vector calculations and keeping track of the positions in row and col.

## Instructions on how to compile/run the simulation
I have provided all the files in the repo `QLearning-ML`. I successfully ran my code in 
Powershell.
If you are running this in WSL then you need Windows X server configured.
- Clone this repo.
- Navigate to the folder `QLearning-ML`.
- To compile the project in java, run command: `javac .\Main.java`
- To execute the project in java, run command: `java Main`
- To run .jar file, run command: `java -jar .\QLearning.jar`
- In case the jar file returns an error (like java.lang.UnsupportedClassVersionError), you can create a new jar file with this command:
    ```
    jar -cfvm QLearning.jar .\Manifest.txt *.class
    ```
  
## Bugs in the simulation
I haven't found any bugs so far. And I have tested it multiple times with multiple grid sizes.
