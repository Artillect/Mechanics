package com.pfaeff_and_cdkrot.api.benchmark;

import com.pfaeff_and_cdkrot.tileentity.TileEntityBenchmark;

import cpw.mods.fml.common.network.Player;

/**
Benchmark action preprocessor.
Runed in server side.
*/
public interface INetworkBenchmarkProcessor
{
	
	/**
	 * Called when text on benchmark changing. Can cancel change event(antispam and such services)
	 * @param tile - tile {provides original text, x, y, z, world}
	 * @param newtext - text setting
	 * @param p - player
	 * @return false to cancel event
	 */
	public boolean onTextChanged(TileEntityBenchmark tile, String newtext, Player p);
	
	/**
	 * Called when bencmark is echos text. Can cancel event
	 * @param tile - tile {world, x, y, z. data}
	 * @param echotext {processed pattern}
	 * @return false to cancel event
	 */
	public boolean onBenchmark(TileEntityBenchmark tile, String echotext);

	/**
	* Called when player requesting an editing GUI open 
	* (gui is open by client side, but text requires to be send from server).
	* @return false to cancel event
	*/
	public boolean requestEditor(TileEntityBenchmark tile, Player p);
}