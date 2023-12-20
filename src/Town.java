/**
 * The Town Class is where it all happens.
 * The Town is designed to manage all the things a Hunter can do in town.
 * This code has been adapted from Ivan Turner's original program -- thank you Mr. Turner!
 */

public class Town {
    // instance variables
    private Hunter hunter;
    private Shop shop;
    private Terrain terrain;
    private String printMessage;
    private boolean toughTown;
    private boolean easy;

    /**
     * The Town Constructor takes in a shop and the surrounding terrain, but leaves the hunter as null until one arrives.
     *
     * @param shop The town's shoppe.
     * @param toughness The surrounding terrain.
     */
    public Town(Shop shop, double toughness, boolean easy) {
        this.shop = shop;
        this.terrain = getNewTerrain();
        this.easy = easy;

        // the hunter gets set using the hunterArrives method, which
        // gets called from a client class
        hunter = null;

        printMessage = "";

        // higher toughness = more likely to be a tough town
        toughTown = (Math.random() < toughness);
    }

    public String getLatestNews() {
        return printMessage;
    }

    /**
     * Assigns an object to the Hunter in town.
     *
     * @param hunter The arriving Hunter.
     */
    public void hunterArrives(Hunter hunter) {
        this.hunter = hunter;
        printMessage = "Welcome to town, " + hunter.getHunterName() + ".";

        if (toughTown) {
            printMessage += "\nIt's pretty rough around here, so watch yourself.";
        } else {
            printMessage += "\nWe're just a sleepy little town with mild mannered folk.";
        }
    }

    /**
     * Handles the action of the Hunter leaving the town.
     *
     * @return true if the Hunter was able to leave town.
     */
    public boolean leaveTown() {
        boolean canLeaveTown = terrain.canCrossTerrain(hunter);
        if (canLeaveTown) {
            String item = terrain.getNeededItem();
            printMessage = "You used your " + "\033[0;35m" + item + " to cross the " + "\033[0;36m" + terrain.getTerrainName() + "\033[0m"+"." + "\033[0m";
            if (checkItemBreak()) {
                hunter.removeItemFromKit(item);
                if(item.equals("horse") || item.equals("rope")){
                    printMessage += "\nUnfortunately, you lost your " + "\033[0;35m" + item  + "\033[0m";
                }
                else{
                    printMessage += "\nUnfortunately, your " + "\033[0;35m" + item + " broke." + "\033[0m";
                }
            }

            return true;
        }

        printMessage = "You can't leave town, " + hunter.getHunterName() + ". You don't have a " + terrain.getNeededItem() + ".";
        return false;
    }

    /**
     * Handles calling the enter method on shop whenever the user wants to access the shop.
     *
     * @param choice If the user wants to buy or sell items at the shop.
     */
    public void enterShop(String choice) {
        shop.enter(hunter, choice);
        printMessage = "";
    }

    /**
     * Gives the hunter a chance to fight for some gold.<p>
     * The chances of finding a fight and winning the gold are based on the toughness of the town.<p>
     * The tougher the town, the easier it is to find a fight, and the harder it is to win one.
     */
    public void lookForTrouble() {
        double noTroubleChance;
        if (toughTown) {
            noTroubleChance = 0.66;
        } else {
            noTroubleChance = 0.33;
        }

        if (Math.random() > noTroubleChance) {
            printMessage = "\033[0;31m" + "You couldn't find any trouble" + "\033[0m";
        } else {
            printMessage = "\033[0;31m" + "You want trouble, stranger!  You got it!\nOof! Umph! Ow!\n" + "\033[0m";
            int goldDiff = (int) (Math.random() * 10) + 1;
            if (Math.random() > noTroubleChance) {
                printMessage += "\033[0;31m" + "Okay, stranger! You proved yer mettle. Here, take my gold." + "\033[0m";
                printMessage += "\033[0;31m" + "\nYou won the brawl and receive " + "\033[0;33m" + goldDiff  + " gold." + "\033[0m";
                hunter.changeGold(goldDiff);
            } else {
                printMessage += "\033[0;31m" + "That'll teach you to go lookin' fer trouble in MY town! Now pay up!" + "\033[0m";
                printMessage += "\033[0;31m" + "\nYou lost the brawl and pay "+ "\033[0;33m" + goldDiff  + " gold." + "\033[0m";
                hunter.changeGold(-goldDiff);

            }
        }
    }
    public void hunt() {
        int rand = (int) (Math.random() * 4) + 1;
            if (rand == 1) {
                System.out.println("You have found a crown!");
                hunter.addItem("crown");
            } else if (rand == 2) {
                System.out.println("You have found a trophy!");
                hunter.addItem("trophy");
            } else if (rand == 3) {
                System.out.println("You have found gem!");
                hunter.addItem("gem");
            } else {
                System.out.println("You have found dust!");
            }
    }

    public String toString() {
        return "This nice little town is surrounded by " + "\033[0;36m" + terrain.getTerrainName() + "\033[0m" + ".";
    }

    /**
     * Determines the surrounding terrain for a town, and the item needed in order to cross that terrain.
     *
     * @return A Terrain object.
     */
    private Terrain getNewTerrain() {
        double rnd =  (int) (Math.random() * 6) + 1;
        if (rnd == 1) {
            return new Terrain("Mountains", "Rope");
        } else if (rnd == 2) {
            return new Terrain("Ocean", "Boat");
        } else if (rnd == 3) {
            return new Terrain("Plains", "Horse");
        } else if (rnd == 4) {
            return new Terrain("Desert", "Water");
        } else if (rnd == 5) {
            return new Terrain("Marsh","boots");
        } else {
            return new Terrain("Jungle", "Machete");
        }
    }

    /**
     * Determines whether a used item has broken.
     *
     * @return true if the item broke.
     */
    private boolean checkItemBreak() {
        double rand = Math.random();
        if(easy){
            rand += 100;
        }
        return (rand < 0.5);
    }
}