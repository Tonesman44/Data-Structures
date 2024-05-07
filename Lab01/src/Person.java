/*
Antonio Fabrizio
Lab 01
2/1/24
Design - Person looks for monster, if it can't see the monster the person hides. Once it sees the monster it moves based off the monster's location.
Constantly moving out of the monsters sight line, making it so the monster has to randomly move
 */

public class Person extends Creature {

    private int currentMonsterLocation;
    private boolean movingTowardsMonster;
    private boolean canSeeMonsterNow;

    public Person(Model model, int row, int column) {
        super(model, row, column);
    }

    /**
     * Controls the movement of the person
     * Returns the direction in which the person wishes to move.
     */

    public int decideMove() {
        //look for monster
        lookForMonster();

        //if monster doesn't see us stay
        if (!canSeeMonsterNow) {
            return Model.STAY;
        }
        // Variable Declarations for Directions
        int SouthWest = Model.turn(currentMonsterLocation, 3);
        int SouthEast = Model.turn(currentMonsterLocation, 5);
        int East = Model.turn(currentMonsterLocation, 6);
        int West = Model.turn(currentMonsterLocation, 2);
        int NorthWest = Model.turn(currentMonsterLocation, 1);
        int NorthEast = Model.turn(currentMonsterLocation, 7);
        int South = Model.turn(currentMonsterLocation, 4);

        //try South West and South East
        if(canMove(SouthWest)){
            return SouthWest;
        }
        else if(canMove(SouthEast)){
            return SouthEast;
        }

        //try East and West
        else if(canMove(East)){
            return East;
        }
        else if(canMove(West)){
            return West;
        }

        //try North West and North East
        else if(canMove(NorthWest)){
            return NorthWest;
        }
        else if(canMove(NorthEast)){
            return NorthEast;
        }

        // Try South
        else if(canMove(South)){
            return South;
        }

        //If can't move away from then stay
        else {
            return Model.STAY;
        }
    }

    private void lookForMonster() {
        // Look around for the monster
        canSeeMonsterNow = false;
        for (int i = Model.MIN_DIRECTION; i <= Model.MAX_DIRECTION; i++) {
            if (look(i) == Model.MONSTER) {
                canSeeMonsterNow = true;
                currentMonsterLocation = i;
            }
        }
    }

}