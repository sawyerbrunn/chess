# chess
Chess Game w/ self-playing AI
Project By: Sawyer Brunn

Description: Chess Engine runnable in a GUI with several levels of chess agents capable of making rational, human-like decisions.
The three levels of agents are as follows:

AI Level 0 - Agent chooses a random move from among all legal moves
AI Level 1 - Agent chooses a rational move that makes the board heuristically better than the current board state
AI Level 2 - Agent uses depth 3 alpha-beta pruning to choose a move that will make the board heuristically optimal up to 3 moves in the future,
assuming opponent plays optimally. Can find guaranteed wins within 3 moves.

Game can be run by running java Chess/main from the directory containing the decompressed chess folder. GUI controls can be found at the top drop down menu.

Updated: 5/22/2021
