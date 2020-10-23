/*
 * File name: Game.cs
 * This file is part of KV-2 Survival game
 * 
 * Created by Iurii Guliev
 * Last modified: Mar 9, 2020
 */

using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class Game : MonoBehaviour
{

    public const float EPS = 0.01f;
    public const float STANDART_G = 9.81f;

    //World borders
    public const float TOP_BRDR = 7 * 2;
    public const float DOWN_BRDR = -7 * 2;
    public const float LEFT_BRDR = -23 * 2;
    public const float RIGHT_BRDR = 23 * 2;

    public static List<GameObject> ForestList = new List<GameObject>();
    public static List<GameObject> EnemyList = new List<GameObject>();

    public static Text scoreText;
    public GameObject EnemyPrefab;
    public GameObject player;
    public static AudioSource Background;
    public static AudioSource LostSound;

    public static float difficulty;

    private static int score;
    private float resetTime = 3.0f;
    private bool reloaded = true;

    private void Awake()
    {
        //ForestList = new List<GameObject>();
        score = 0;
        scoreText = GameObject.Find("ScoreText").GetComponent<Text>();
        difficulty = PlayerPrefs.GetFloat("Difficulty");
        resetTime += (1.0f - difficulty) * 3;
        AudioSource[] audios = GetComponents<AudioSource>();
        Background = audios[0];
        LostSound = audios[1];
    }

    // Start is called before the first frame update
    void Start()
    {
        Background.Play();
    }

    // Update is called once per frame
    void Update()
    {
        if (reloaded)
        {
            SpawnEnemy();
            reloaded = false;
            StartCoroutine(ReloadDelay());
        }
    }
    IEnumerator ReloadDelay()
    {
        yield return new WaitForSeconds(resetTime);
        reloaded = true;
    }

    public static bool checkBorders(Vector2 coord)
    {
        if (coord.x > LEFT_BRDR && coord.x < RIGHT_BRDR && coord.y < TOP_BRDR && coord.y > DOWN_BRDR)
            return true;
        return false;
    }

    public static void RestartGame()
    {
        LostSound.Play();
        score = 0;
        scoreText.text = "Score: " + score;
        foreach (var enemy in Game.EnemyList)
        {
            Destroy(enemy);
        }
        EnemyList.Clear();
    }

    public static void AddScore()
    {
        score++;
        scoreText.text = "Score: " + score;
    }

    //Creates an enemy object in a random position around the player
    public void SpawnEnemy()
    {
        Vector3 offset = new Vector3(0, 16, 0);
        offset = Quaternion.AngleAxis(Random.Range(0.0f, 360.0f), Vector3.forward) * offset;
        Instantiate(EnemyPrefab, player.transform.position + offset, new Quaternion());
    }
}
