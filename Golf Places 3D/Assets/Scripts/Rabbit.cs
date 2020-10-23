using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Rabbit : MonoBehaviour
{
    public Animator rabbitAnimator;

    public AudioSource rabbitDeath;

    private bool dead = false;
    private bool rotate = false;

    private AnimationClip clip;
    // Start is called before the first frame update
    void Start()
    {
        clip = rabbitAnimator.runtimeAnimatorController.animationClips[1];
        AnimationEvent evt = new AnimationEvent();
        evt.time = 1.3f;
        evt.functionName = "RabbitRotate";
        clip.AddEvent(evt);

        rabbitAnimator.SetBool("isRunning", true);
    }

    // Update is called once per frame
    void Update()
    {
        if (rotate)
        {
            transform.Rotate(0.0f, 90.0f, 0.0f, Space.World);
            rotate = false;
        }
    }

    private void OnCollisionEnter(Collision collision)
    {
        if (collision.gameObject.tag == "Ball")
        {
            if (dead == false)
            {
                rabbitAnimator.SetBool("isRunning", false);
                rabbitAnimator.SetBool("isDead", true);
                dead = true;
                rabbitDeath.Play();
            }
        }
    }

    public void RabbitRotate()
    {
        Debug.Log("Triggered");
        if (dead == false)
        {
            rotate = true;
        }
    }
}
