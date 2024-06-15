*This document contains the project description for the Modrinth website*

# Cross Your Bows

Cross Your Bows allows you to configure crossbow behaviour to your heart's content.

You can create multiple crossbow configurations, that can be applied to individual (or all) crossbows.

## Examples

### Infinity for Crossbows

The below example enables a similar effect to the 'infinity' enchantment, for all crossbows when shooting arrows.

```yaml
require_permission: false # Disable permission checking, settings will apply to every player regardless of permission
fire_configurations:
  default: # the 'default' configuration applies to all bows
    consume_item: false # don't consume the projectile when charged
    pickup_last_projectile: false # make the projectile not pickup-able (only applies to arrows)
    enable_projectile: arrow # only apply settings to arrows, fireworks behave like normal
    # use 'firework' instead of 'arrow' to apply only to fireworks
    # use 'both' to apply to both (this is the default)
```

### Fast Firing Crossbow

The below example makes bows with the 'example_machine_gun' configuration able to quickly fire 20 arrows.

Run `/crossyourbows apply example_machine_gun` with a crossbow in your hand to apply the configuration.

```yaml
fire_configurations:
  example_machine_gun:
    consume_item: true # consume the projectile item when charged (default is true)
    fire_cooldown: 3 # 3 tick cooldown after firing
    max_charges: 20 # fire 20 arrows before needing to charge again (the 'clip' size)
    allow_projectile: arrow # fireworks won't be able to be charged
    pickup_last_projectile: false # last shot arrow in 'clip' cannot be picked up again
    arrow:
      knockback: 2 # arrows deal extra knockback
      damage: 5.0 # arrows deal 5.0 points of damage
      pierce: 2 # arrows pierce 2 entities
      critical: false # arrows are not critical (default is true for crossbows, which gives extra damage)
```

## All Settings

**In the future, this section will describe all the configuration options and permissions in detail.**
For now, use the examples as a reference, they cover all the currently available settings.

## Future plans

- Configurable arrow damage cooldown (for making fast firing crossbows actually useful).
- Configurable fire patterns (fire multiple arrows, e.g. for making a shotgun).
- Configurable arrow speed and (if it is possible) damage falloff.

## The End

Thank you for reading, and have a nice day!