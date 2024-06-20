*This document contains the project description for the Modrinth website*

# Cross Your Bows

Cross Your Bows allows you to configure crossbow behaviour to your heart's content.
You can create multiple crossbow configurations, that can be applied to individual crossbows.

Crossbows have never been this powerful!

## Versions

Cross Your Bows is currently updated for Minecraft 1.20.6.
The plugin will only support the latest (non-experimental) version, with a small margin.

**Minecraft 1.21 cannot be supported until Paper finishes work on the item changes!**

## Examples

For the options to work, remember to give the right permissions,
or to disable `require_permission` in the config.

### Infinity for Crossbows

The below example enables a similar effect to the 'infinity' enchantment, for all crossbows when shooting arrows.

```yaml
require_permission: false # Disable permission checking, settings will apply to every player regardless of permission
bow_configurations:
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
require_permission: false # Disable permission checking, settings will apply to every player regardless of permission
arrow_damage_cooldown: false # disable damage cooldown for all arrows
firework_damage_cooldown: true # keep damage cooldown on for fireworks (default is true)
bow_configurations:
  example_machine_gun:
    consume_item: true # consume the projectile item when charged (default is true)
    fire_cooldown: 3 # 3 tick cooldown after firing
    max_charges: 20 # fire 20 arrows before needing to charge again (the 'clip' size)
    allow_projectile: arrow # fireworks won't be able to be charged
    pickup_last_projectile: false # last shot arrow in 'clip' cannot be picked up again
    display:
      name: <red>Trigger Happy</red> # Apply a custom name to the item
      lore: # Apply a list of custom lore to the item
        - <gray>Destruction at a rapid interval</gray>
    arrow:
      speed: 0 # speed stays the same as vanilla, set to a higher (or lower) value to modify speed (default is 0)
      knockback: 2 # arrows deal extra knockback
      damage: 5.0 # arrows deal 5.0 points of damage
      pierce: 2 # arrows pierce 2 entities
      critical: false # arrows are not critical (default is true for crossbows, which gives extra damage)
```

## Permissions

*These are the permissions the plugin uses.*

**crossyourbows.command**

Allows the usage of the `/crossyourbows` command.
This includes all subcommands.

**crossyourbows.usage**

When `require_permission` is enabled, this permission is required for the custom bow settings to apply.
When a player does not have the permission, crossbows will work like vanilla for them.

## Subcommands

*These are the subcommands for the `/crossyourbows` command (alias `/cb`).*

**/crossyourbows reload**

Read all the settings from the plugin config, and enable them.
Use this to apply changes made to the `config.yml` when the server is running.

**/crossyourbows apply <config_name>**

Apply a named configuration from `bow_configurations` to a bow in your hand.
The bow can't be charged when running this.

**/crossyourbows reset**

Reset the configuration of the bow in your hand.
The bow will work like vanilla again (or the `default` bow configuration, if present).

## Configuration

*These are the options that can be configured in the plugin configuration file.*

**require_permission**: `true` or `false`

When enabled, players are required to have the **crossyourbows.usage** permission in order to have the bow settings
applied.

**arrow_damage_cooldown**: `true` or `false`

When disabled, entities hit with an arrow will have their damage cooldown reset.
That means they can immediately take damage again, from any source.

**firework_damage_cooldown**: `true` or `false`

When disabled, entities hit with a firework explosion will have their damage cooldown reset.
That means they can immediately take damage again, from any source.

**bow_configurations**: contains multiple sections

Each section added here will be a new custom bow configuration, that you can apply with the `apply` subcommand.
This excludes the section named `default`, which will apply to all crossbows without a configuration applied.

Each bow configuration has the following options:

> **consume_item**: `true` or `false`
>
> Whether to consume the projectile item when charging.
>
> **fire_cooldown**: whole number
>
> Cooldown given to the item after firing, in ticks.
>
> **max_charges**: whole number
>
> How many times the crossbow can fire before running out of projectiles.
> Think of it as the 'clip' size.
>
> **allow_projectile**: `arrow`, `firework` or `both`
>
> Which projectile type to allow to be charged in the crossbow.
>
> **enable_projectile**: `arrow`, `firework` or `both`
>
> Which projectile type to enable the settings for.
> The other type can still be charged (if allowed) but will act like vanilla.
>
> **pickup_last_projectile**: `true` or `false`
>
> Whether the last arrow shot with the bow can be picked up again.
> All other shot arrows cannot be picked up by default.
>
> **display**: section
>
> The display section has the following options:
>
> > **name**: formatted string (minimessage)
> >
> > The item name of the crossbow.
> >
> > **lore**: list of formatted strings (minimessage)
> >
> > A list of strings to add to the lore of the crossbow.
>
> **arrow**: section
>
> The arrow section has the following options:
>
> > **speed**: number
> >
> > The speed of the shot arrow.
> >
> > **knockback**: number
> >
> > The knockback of the shot arrow.
> >
> > **damage**: number
> >
> > The damage caused by the shot arrow.
> >
> > **pierce**: whole number
> >
> > The amount of entities the arrow can pierce.
> >
> > **critical**: `true` or `false`
> >
> > Whether the arrow is considered critical.
> > This will cause extra damage and the 'crit' particle effects on the arrow.

## The End

Thank you for reading, and have a nice day!