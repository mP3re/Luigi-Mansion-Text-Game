
/**
 * Write a description of class NPC here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class NPC
{
    // instance variables - replace the example below with your own
    private String NPCName;
    private String dialogueBefore;
    private String dialogueAfter;
    private boolean changeDialogue;

    /**
     * Constructor for objects of class NPC
     */
    public NPC(String name,String dialogueBefore,String dialogueAfter,boolean changeDialogue)
    {
        this.NPCName=name;
        this.dialogueBefore=dialogueBefore;
        this.dialogueAfter=dialogueAfter;
        this.changeDialogue=changeDialogue;
    }

    /**
     * Get name of NPC
     */
    public String getName()
    {
        return NPCName;
    }
    
    public boolean getChangeDialogue()
    {
        return changeDialogue;
    }
    
    /**
     * Get dialogue of NPC. this is meant to change depending on the NPC 
     * Usually, the dialogue should change upon talking to them for a second time
     * Some NPCs have certain conditions before they change dialogue ie giving them an object
     */
    public void getDialogue()
    {
        if (this.changeDialogue==false){
            System.out.println(dialogueBefore);
        }
        else{
            System.out.println(dialogueAfter);
        }
    }
    
    /**
     * change/ set the next dialogue option
     */
    public void setChangeDialogue(boolean switchTo){
        changeDialogue=switchTo;
    }
    
}
