package org.halemaster.flexibilities;

import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.item.inventory.UseItemStackEvent;
import org.spongepowered.api.text.Text;

import java.util.Optional;

public class ItemEvents
{
    public static final String ABILITY_NAME = "ABILITY: ";

    private AbilitiesPlugin plugin;

    public ItemEvents(AbilitiesPlugin plugin)
    {
        this.plugin = plugin;
    }

    @Listener
    public void onItemUse(UseItemStackEvent.Finish useItemStackEvent)
    {
        Optional<Text> displayText= useItemStackEvent.getItemStackInUse()
                .getOriginal().createStack().get(Keys.DISPLAY_NAME);
        if(displayText.isPresent())
        {
            String displayName = displayText.get().toPlain();
            if(displayName.startsWith(ABILITY_NAME))
            {
                String abilityName = displayName.substring(ABILITY_NAME.length()).trim();
                Optional<Ability> ability = Ability.loadAbility(plugin.getAbilitiesNode(), abilityName);
                if(ability.isPresent())
                {
                    // use ability!
                }
            }
        }
    }
}
