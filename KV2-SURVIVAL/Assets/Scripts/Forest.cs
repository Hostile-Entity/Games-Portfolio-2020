/*
 * File name: Forest.cs
 * This file is part of KV-2 Survival game
 * 
 * Created by Iurii Guliev
 * Last modified: Mar 10, 2020
 */

using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Forest : MonoBehaviour
{
    // Start is called before the first frame update
    private void Awake()
    {
        Game.ForestList.Add(gameObject);
    }

    // Update is called once per frame
    void Update()
    {
        
    }
}
