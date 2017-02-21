package com.xzzpig.piginstance;

import com.github.xzzpig.pigapi.bukkit.TCommandHelp;

public class Help {
	public static TCommandHelp _PigInstance = new TCommandHelp("PigInstance", "PigInstance的主命令", "/pi help")
			.setPermission("piginstance.command.*");
	public static TCommandHelp pi_cancel = _PigInstance.addSubCommandHelp("cancel", "取消执行某命令", null, null)
			.setCommandRunner(CommandRunners::pi_cancel);
	public static TCommandHelp pi_confirm = _PigInstance.addSubCommandHelp("confirm", "确认执行某命令", null, null)
			.setCommandRunner(CommandRunners::pi_confirm);
	public static TCommandHelp pi_ins = _PigInstance.addSubCommandHelp("ins", "管理副本区域", "/pi ins help", "");
	public static TCommandHelp pi_ins_add = pi_ins
			.addSubCommandHelp("add", "将选定的区域加入副本区域", "/pi ins add [name]", "[name]")
			.setCommandRunner(CommandRunners::pi_ins_add).addLimit(TCommandHelp.isPlayer);
	public static TCommandHelp pi_ins_create = pi_ins
			.addSubCommandHelp("create", "创建新的副本区域", "/pi ins create [name]", "[name]")
			.setCommandRunner(CommandRunners::pi_ins_create).setPermission("piginstance.command.ins.create");
	public static TCommandHelp pi_ins_delete = pi_ins
			.addSubCommandHelp("delete", "删除副本区域", "/pi ins delete [name]", "[name]")
			.setCommandRunner(CommandRunners::pi_ins_delete);
	public static TCommandHelp pi_ins_except = pi_ins
			.addSubCommandHelp("except", "将选定的区域排除副本区域", "/pi ins except [name]", "[name]")
			.setCommandRunner(CommandRunners::pi_ins_except).addLimit(TCommandHelp.isPlayer);
	public static TCommandHelp pi_ins_info = pi_ins.addSubCommandHelp("info", "获取副本区域信息", "/pi ins info", "[name]")
			.setCommandRunner(CommandRunners::pi_ins_info);
	public static TCommandHelp pi_ins_list = pi_ins.addSubCommandHelp("list", "列出所在的副本区域", "/pi ins list", null)
			.setCommandRunner(CommandRunners::pi_ins_list).addLimit(TCommandHelp.isPlayer);
	public static TCommandHelp pi_ins_listall = pi_ins
			.addSubCommandHelp("listall", "列出所有的副本区域", "/pi ins listall", null)
			.setCommandRunner(CommandRunners::pi_ins_listall);
	public static TCommandHelp pi_ins_image = pi_ins
			.addSubCommandHelp("image", "将副本区域输出为简单图片", "/pi ins image [name]", "[name]")
			.setCommandRunner(CommandRunners::pi_ins_image);
}
