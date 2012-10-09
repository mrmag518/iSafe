package com.mrmag518.iSafe.Util;

import com.mrmag518.iSafe.Files.Messages;
import com.mrmag518.iSafe.Files.iSafeConfig;
import com.mrmag518.iSafe.iSafe;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PermHandler {
    /*public boolean checkingUpdatePerms = false;
    public boolean cancelDamagePerms = false;
    public boolean checkingSpamPerms = false;
    public boolean checkingFullbrightPerms = false;
    
    public boolean hasPermission(CommandSender p, String permission) {
        if (iSafeConfig.getISafeConfig().getBoolean("UseVaultForPermissions", true)) {
            if (iSafe.perms.has(p, permission)) {
                return true;
            } else {
                Messages.noCmdPermission(p);
                return false;
            }
        } else {
            if (p.hasPermission(permission)) {
                return true;
            } else {
                Messages.noCmdPermission(p);
                return false;
            }
        }
    }

    public boolean hasBlacklistPermission(Player p, String permission) {
        if (iSafeConfig.getISafeConfig().getBoolean("UseVaultForPermissions", true)) {
            if (iSafe.perms.has(p, permission)) {
                return true;
            } else {
                return false;
            }
        } else {
            if (p.hasPermission(permission)) {
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean hasPermission(Player p, String permission) {
        if (iSafeConfig.getISafeConfig().getBoolean("UseVaultForPermissions", true)) {
            if (iSafe.perms.has(p, permission)) {
                return true;
            } else {
                if(shallOutputNoPerm() == false) {
                    // ignore.
                } else {
                    Messages.noPermission(p);
                }
                return false;
            }
        } else {
            if (p.hasPermission(permission)) {
                return true;
            } else {
                if(shallOutputNoPerm() == false) {
                    // ignore.
                } else {
                    Messages.noPermission(p);
                }
                return false;
            }
        }
    }
    
    private boolean shallOutputNoPerm() {
        if(checkingUpdatePerms == true
            || checkingSpamPerms == true
            || checkingFullbrightPerms == true) 
        {
            checkingUpdatePerms = false;
            checkingSpamPerms = false;
            checkingFullbrightPerms = false;
            return false;
        } else {
            return true;
        }
    }*/
}
